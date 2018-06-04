/*
 * Moxy - Lean-and-mean mocking framework for Java with a fluent API.
 *
 * Copyright 2018 Ross Bamford
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included
 *   in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.roscopeco.moxy.impl.asm.visitors;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.Mock;

/**
 * Creates mocks from interfaces.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyMockInterfaceVisitor extends AbstractMoxyTypeVisitor {
  private final String interfaceInternalName;

  public MoxyMockInterfaceVisitor(final Class<?> iface) {
    super(AbstractMoxyTypeVisitor.makeMockName(iface));

    this.interfaceInternalName = Type.getInternalName(iface);
  }

  @Override
  public void visit(final int version, final int access, final String name, final String signature, final String superName,
      final String[] originalInterfaces) {
    final ArrayList<String> newInterfaces = new ArrayList<>(originalInterfaces.length + 2);
    newInterfaces.add(this.interfaceInternalName);
    newInterfaces.add(MOXY_SUPPORT_INTERFACE_INTERNAL_NAME);
    newInterfaces.addAll(Arrays.asList(originalInterfaces));

    // Start the class visit
    super.visit(version,
                access & ~ACC_ABSTRACT & ~ACC_INTERFACE | ACC_SUPER,
                this.getNewClassInternalName(),
                signature,
                OBJECT_INTERNAL_NAME,
                newInterfaces.toArray(new String[newInterfaces.size()]));

    // Add the IsMock annotation
    this.visitAnnotation(Type.getDescriptor(Mock.class), true).visitEnd();
  }

  @Override
  public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
    // Do the mocking
    return new MoxyMockingMethodVisitor(this.cv.visitMethod(
        access & ~ACC_ABSTRACT | ACC_SYNTHETIC,
        name, desc, signature, exceptions),
    this.interfaceInternalName,
    name,
    desc,
    Type.getReturnType(desc),
    Type.getArgumentTypes(desc),
    true);
  }

  @Override
  public void visitEnd() {
    // Manually generate a constructor in case user wants to manually instantiate,
    // like they do for partial mocks...
    final MoxyPassThroughConstructorVisitor mv =
        new MoxyPassThroughConstructorVisitor(this.cv.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC,
                            INIT_NAME,
                            MOCK_CONSTRUCTOR_DESCRIPTOR,
                            null,
                            null),
            OBJECT_INTERNAL_NAME,
            this.getNewClassInternalName(),
            VOID_VOID_DESCRIPTOR,
            EMPTY_TYPE_ARRAY);

    mv.visitCode();
    mv.visitEnd();

    super.visitEnd();
  }
}
