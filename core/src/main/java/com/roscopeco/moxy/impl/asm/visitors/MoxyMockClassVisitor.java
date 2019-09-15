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

import com.roscopeco.moxy.api.MoxyMock;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

/**
 * Creates mocks from classes.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyMockClassVisitor extends AbstractMoxyTypeVisitor {
    private final Class<?> originalClass;
    private final String originalClassInternalName;

    public MoxyMockClassVisitor(final Class<?> originalClass, final Map<String, Method> mockableMethods) {
        super(AbstractMoxyTypeVisitor.makeMockName(originalClass), mockableMethods);

        this.originalClass = originalClass;
        this.originalClassInternalName = Type.getInternalName(originalClass);
    }

    @Override
    public void visit(final int version,
                      final int access,
                      final String name,
                      final String signature,
                      final String superName,
                      final String[] originalInterfaces) {
        final ArrayList<String> newInterfaces = new ArrayList<>(originalInterfaces.length + 1);
        newInterfaces.add(MOXY_SUPPORT_INTERFACE_INTERNAL_NAME);
        newInterfaces.addAll(Arrays.asList(originalInterfaces));

        super.visit(version,
                access & ~ACC_ABSTRACT | ACC_SYNTHETIC | ACC_SUPER,
                super.getNewClassInternalName(),
                signature,
                this.originalClassInternalName,
                newInterfaces.toArray(new String[0]));

        this.visitAnnotation(Type.getDescriptor(MoxyMock.class), true).visitEnd();
    }

    /*
     * Used when generating constructors, inserts MoxyEngine as the first argument
     * in a method descriptor.
     */
    private String prependMethodArgsDescriptorWithEngine(final String descriptor) {
        if (descriptor == null) {
            return MOCK_CONSTRUCTOR_DESCRIPTOR;
        } else {
            return "(" + MOXY_ENGINE_DESCRIPTOR + descriptor.substring(1);
        }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final boolean isAbstract = (access & ACC_ABSTRACT) != 0;
        final boolean isNative = (access & ACC_NATIVE) != 0;
        final boolean isStatic = (access & ACC_STATIC) != 0;
        final boolean isSynthetic = (access & ACC_SYNTHETIC) != 0 || (access & ACC_BRIDGE) != 0;

        if (INIT_NAME.equals(name)) {
            // Generate pass-through constructors
            final String newDesc = this.prependMethodArgsDescriptorWithEngine(desc); /* this is the new descriptor */
            return new MoxyPassThroughConstructorVisitor(this.cv.visitMethod(access & ~ACC_ABSTRACT & ~ACC_NATIVE,
                    name, newDesc, signature, exceptions),
                    this.originalClassInternalName,
                    this.getNewClassInternalName(),
                    desc,    /* this is the super descriptor */
                    Type.getArgumentTypes(desc));
        } else {
            // Always mock abstract methods (or it won't verify), decide for concrete based on mockMethods.
            if (!isStatic && !isSynthetic && (isAbstract || this.isToMock(name, desc))) {
                // mark as mocked
                this.markMethodAsMocked(name, desc);

                // Do the mocking
                return new MoxyMockingMethodVisitor(this.cv.visitMethod(access & ~ACC_ABSTRACT & ~ACC_NATIVE | ACC_SYNTHETIC,
                        name, desc, signature, exceptions),
                        this.originalClass,
                        name,
                        desc,
                        Type.getReturnType(desc),
                        Type.getArgumentTypes(desc),
                        isAbstract,
                        isNative);
            } else {
                // Don't mock, just super.
                return null;
            }
        }
    }
}
