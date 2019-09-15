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
package com.roscopeco.moxy.impl.asm.visitors.classmock;

import com.roscopeco.moxy.impl.asm.visitors.AbstractMoxyMockMethodVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.MOXY_SUPPORT_INTERFACE_INTERNAL_NAME;
import static com.roscopeco.moxy.impl.asm.classmock.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

/*
 * Generates the mock methods in-place on the original class.
 */
class MoxyClassMockingMethodVisitor extends AbstractMoxyMockMethodVisitor {
    private final String delegateClass;
    private final boolean isStatic;

    @SuppressWarnings("squid:S00107" /* This internal class requires these parameters */)
    MoxyClassMockingMethodVisitor(final MethodVisitor delegate,
                                  final Class<?> originalClass,
                                  final String delegateClass,
                                  final String methodName,
                                  final String methodDescriptor,
                                  final Type returnType,
                                  final Type[] argTypes,
                                  final boolean isStatic) {
        super(delegate, originalClass, methodName, methodDescriptor, returnType, argTypes, false, false);
        this.delegateClass = delegateClass;
        this.isStatic = isStatic;
    }

    @Override
    protected int getFirstArgumentLocalSlot() {
        return this.isStatic ? 0 : 1;
    }

    @Override
    protected void generateLoadMockSupport() {
        if (this.isStatic) {
            this.delegate.visitLdcInsn(Type.getType(this.originalClass));

            this.delegate.visitMethodInsn(INVOKESTATIC,
                    INSTANCE_REGISTRY_INTERNAL_NAME,
                    REGISTRY_GET_STATIC_DELEGATE_METHOD_NAME,
                    REGISTRY_GET_STATIC_DELEGATE_DESCRIPTOR,
                    false);

            this.delegate.visitTypeInsn(CHECKCAST, MOXY_SUPPORT_INTERFACE_INTERNAL_NAME);
        } else {
            this.delegate.visitVarInsn(ALOAD, 0);

            this.delegate.visitMethodInsn(INVOKESTATIC,
                    INSTANCE_REGISTRY_INTERNAL_NAME,
                    REGISTRY_GET_DELEGATE_METHOD_NAME,
                    REGISTRY_GET_DELEGATE_DESCRIPTOR,
                    false);

            this.delegate.visitTypeInsn(CHECKCAST, this.delegateClass);
        }
    }

    @Override
    protected void generateRealMethodCall() {
        if (!this.isStatic) {
            // load support
            this.generateLoadMockSupport();
        }

        // load arguments
        this.generateLoadMethodArguments();

        this.delegate.visitMethodInsn(
                this.isStatic ? INVOKESTATIC : INVOKEVIRTUAL,
                this.delegateClass,
                this.methodName,
                this.methodDescriptor,
                false);
    }

    @Override
    public void visitCode() {
        super.generatePreamble();
        super.generateReturn();
    }
}
