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

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

class MoxyMockingMethodVisitor extends AbstractMoxyMockMethodVisitor {
  MoxyMockingMethodVisitor(final MethodVisitor delegate,
                           final Class<?> originalClass,
                           final String methodName,
                           final String methodDescriptor,
                           final Type returnType,
                           final Type[] argTypes,
                           final boolean wasAbstract) {
    super(delegate, originalClass, methodName, methodDescriptor, returnType, argTypes, wasAbstract);
  }

  @Override
  protected int getFirstArgumentLocalSlot() {
    return 1;
  }

  @Override
  protected void generateLoadMockSupport() {
    this.delegate.visitVarInsn(ALOAD, 0);
  }

  @Override
  protected void generateRealMethodCall() {
    // load support
    this.generateLoadMockSupport();

    // load arguments
    this.generateLoadMethodArguments();

    this.delegate.visitMethodInsn(INVOKESPECIAL,
        this.originalClassInternalName,
        this.methodName,
        this.methodDescriptor, false);
  }
}
