/*
 * MoxyClassMockConstructorVisitor.java -
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

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static com.roscopeco.moxy.impl.asm.classmock.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.impl.asm.visitors.AbstractMoxyMockMethodVisitor;

/**
 * TODO Document MoxyClassMockConstructorVisitor
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyClassMockConstructorVisitor extends AbstractMoxyMockMethodVisitor {
  private final String delegateClassInternal;

  MoxyClassMockConstructorVisitor(final MethodVisitor delegate,
                                  final Class<?> thisClass,
                                  final String delegateClassInternal,
                                  final int access,
                                  final String name,
                                  final String descriptor,
                                  final Type returnType,
                                  final Type[] argTypes) {
    super(delegate, thisClass, name, descriptor, returnType, argTypes, false);
    this.delegateClassInternal = delegateClassInternal;
  }

  @Override
  public void visitCode() {
    this.delegate.visitVarInsn(ALOAD, 0);
    this.delegate.visitInsn(DUP);
    this.delegate.visitMethodInsn(INVOKESPECIAL,
                                  "java/lang/Object", // this.thisClassInternal,
                                  INIT_NAME,
                                  VOID_VOID_DESCRIPTOR,
                                  false);

    this.delegate.visitTypeInsn(NEW, this.delegateClassInternal);
    this.delegate.visitInsn(DUP);
    this.delegate.visitInsn(DUP);
    this.generateLoadMethodArguments();
    this.delegate.visitMethodInsn(INVOKESPECIAL,
                                  this.delegateClassInternal,
                                  INIT_NAME,
                                  this.methodDescriptor,
                                  false);

    this.delegate.visitTypeInsn(NEW, MOXY_SUPPORT_IVARS_INTERNAL_NAME);
    this.delegate.visitInsn(DUP);
    this.delegate.visitMethodInsn(INVOKESTATIC,
                                  MOXY_INTERNAL_NAME,
                                  MOXY_GETENGINE_METHOD_NAME,
                                  MOXY_GETENGINE_DESCRIPTOR,
                                  false);

    this.delegate.visitTypeInsn(CHECKCAST, MOXY_ASM_ENGINE_INTERNAL_NAME);

    this.delegate.visitMethodInsn(INVOKESPECIAL,
                                  MOXY_SUPPORT_IVARS_INTERNAL_NAME,
                                  INIT_NAME,
                                  VOID_MOXYENGINE_DESCRIPTOR,
                                  false);

    this.delegate.visitFieldInsn(PUTFIELD,
                                 this.delegateClassInternal,
                                 SUPPORT_IVARS_FIELD_NAME,
                                 MOXY_SUPPORT_IVARS_DESCRIPTOR);

    this.delegate.visitMethodInsn(INVOKESTATIC,
                                  INSTANCE_REGISTRY_INTERNAL_NAME,
                                  REGISTRY_REGISTER_DELEGATE_METHOD_NAME,
                                  REGISTRY_REGISTER_DELEGATE_DESCRIPTOR,
                                  false);

    this.delegate.visitInsn(RETURN);
  }

  @Override
  protected void generateLoadMockSupport() {
    // TODO Auto-generated method stub

  }

  @Override
  protected void generateRealMethodCall() {
    // TODO Auto-generated method stub

  }


}
