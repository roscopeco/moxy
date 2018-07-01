/*
 * MoxyClassMockAdapter.java -
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

package com.roscopeco.moxy.impl.asm.visitors.classmock;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.MoxyMock;

/**
 * TODO Document MoxyClassMockAdapter
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyClassMockAdapter extends ClassVisitor {
  private final Class<?> thisClz;
  private final String delegateClzInternal;

  public MoxyClassMockAdapter(final ClassVisitor delegate, final Class<?> thisClz, final Class<?> delegateClz) {
    super(ASM6, delegate);
    this.thisClz = thisClz;
    this.delegateClzInternal = Type.getInternalName(delegateClz);
  }

  @Override
  public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);

    this.visitAnnotation(Type.getDescriptor(MoxyMock.class), true).visitEnd();
  }

  @Override
  public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
    final boolean isAbstract = (access & ACC_ABSTRACT) != 0;
    final boolean isStatic = (access & ACC_STATIC) != 0;

    if (isAbstract) {
      return super.visitMethod(access, name, desc, signature, exceptions);
    } else if ("<init>".equals(name)) {
      return new MoxyClassMockConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions),
                                                 this.thisClz,
                                                 this.delegateClzInternal,
                                                 access,
                                                 name,
                                                 desc,
                                                 Type.getReturnType(desc),
                                                 Type.getArgumentTypes(desc));
    } else {
      return new MoxyClassMockingMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions),
                                               this.thisClz,
                                               this.delegateClzInternal,
                                               name,
                                               desc,
                                               Type.getReturnType(desc),
                                               Type.getArgumentTypes(desc),
                                               isStatic);
    }
  }
}
