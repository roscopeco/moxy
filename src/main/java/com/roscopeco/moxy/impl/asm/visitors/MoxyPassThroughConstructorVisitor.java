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

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class MoxyPassThroughConstructorVisitor extends MethodVisitor {
  private final MethodVisitor delegate;
  private final String originalClass;
  private final String generatingClass;
  private final Type[] argTypes;
  private final String methodDescriptor;

  public MoxyPassThroughConstructorVisitor(final MethodVisitor delegate,
                                  final String originalClass,
                                  final String generatingClass,
                                  final String methodDescriptor,
                                  final Type[] argTypes) {
    // don't pass the delegate to the super constructor, or we'll generate
    // both old and new bytecode. Instead, just use local mv field and
    // stash the delegate directly.
    super(ASM6);
    this.delegate = delegate;
    this.originalClass = originalClass;
    this.generatingClass = generatingClass;
    this.methodDescriptor = methodDescriptor;
    this.argTypes = argTypes;
  }

  @Override
  public void visitCode() {
    // one for super call, one for engine store
    this.delegate.visitVarInsn(ALOAD, 0);
    this.delegate.visitInsn(DUP);

    // super call
    int currentSlot = 2;
    for (int argNum = 0; argNum < this.argTypes.length; argNum++) {
      final char argType = this.argTypes[argNum].toString().charAt(0);
      switch (argType) {
      case BYTE_PRIMITIVE_INTERNAL_NAME:
      case CHAR_PRIMITIVE_INTERNAL_NAME:
      case SHORT_PRIMITIVE_INTERNAL_NAME:
      case INT_PRIMITIVE_INTERNAL_NAME:
      case BOOL_PRIMITIVE_INTERNAL_NAME:
        this.delegate.visitVarInsn(ILOAD, currentSlot);
        currentSlot += 1;
        break;
      case LONG_PRIMITIVE_INTERNAL_NAME:
        this.delegate.visitVarInsn(LLOAD, currentSlot);
        currentSlot += 2;
        break;
      case FLOAT_PRIMITIVE_INTERNAL_NAME:
        this.delegate.visitVarInsn(FLOAD, currentSlot);
        currentSlot += 1;
        break;
      case DOUBLE_PRIMITIVE_INTERNAL_NAME:
        this.delegate.visitVarInsn(DLOAD, currentSlot);
        currentSlot += 2;
        break;
      case ARRAY_PRIMITIVE_INTERNAL_NAME:
      case OBJECT_PRIMITIVE_INTERNAL_NAME:
        this.delegate.visitVarInsn(ALOAD, currentSlot);
        currentSlot += 1;
        break;
      default:
        throw new IllegalArgumentException("Unrecognised JVM primitive type: '" + argType + "'.\n"
            + "Your JVM must be super-new and improved.\n"
            + "To fix, add mysterious new type to switch in MoxyPassThroughConstructorVisitor#visitCode()");
      }
    }

    this.delegate.visitMethodInsn(INVOKESPECIAL, this.originalClass, INIT_NAME, this.methodDescriptor, false);

    // Create ivars object
    this.delegate.visitTypeInsn(NEW, MOXY_SUPPORT_IVARS_INTERNAL_NAME);
    this.delegate.visitInsn(DUP);

    // construct with engine
    this.delegate.visitVarInsn(ALOAD, 1);
    this.delegate.visitTypeInsn(CHECKCAST, MOXY_ASM_ENGINE_INTERNAL_NAME);
    this.delegate.visitMethodInsn(INVOKESPECIAL,
                                  MOXY_SUPPORT_IVARS_INTERNAL_NAME,
                                  INIT_NAME,
                                  SUPPORT_ivars_CTOR_DESCRIPTOR,
                                  false);

    // Store in field
    this.delegate.visitFieldInsn(PUTFIELD, this.generatingClass, SUPPORT_ivars_FIELD_NAME, MOXY_SUPPORT_ivars_DESCRIPTOR);

    // Done
    this.delegate.visitInsn(RETURN);
  }
}
