package com.roscopeco.moxy.impl.asm.visitors;

import static org.objectweb.asm.Opcodes.*;
import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;

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
    // one for super call, one for field store, one each for throws/return map
    this.delegate.visitVarInsn(ALOAD, 0);
    this.delegate.visitInsn(DUP);    
    this.delegate.visitInsn(DUP);    
    this.delegate.visitInsn(DUP);    
    
    // super call
    int currentSlot = 2;
    for (int argNum = 0; argNum < this.argTypes.length; argNum++) {
      char argType = this.argTypes[argNum].toString().charAt(0);
      switch (argType) {
      case 'B':
      case 'C':
      case 'S':
      case 'I':
      case 'Z':
        this.delegate.visitVarInsn(ILOAD, currentSlot);
        currentSlot += 1;
        break;
      case 'J':
        this.delegate.visitVarInsn(LLOAD, currentSlot);
        currentSlot += 2;
        break;
      case 'F':
        this.delegate.visitVarInsn(FLOAD, currentSlot);
        currentSlot += 1;
        break;
      case 'D':
        this.delegate.visitVarInsn(DLOAD, currentSlot);
        currentSlot += 2;
        break;
      case 'L':
        this.delegate.visitVarInsn(ALOAD, currentSlot);
        currentSlot += 1;
        break;
      default:
        throw new IllegalArgumentException("Unrecognised JVM primitive type: '" + argType + "'.\n"
            + "Your JVM must be super-new and improved.\n"
            + "To fix, add mysterious new type to switch in MoxyPassThroughConstructorVisitor#visitCode()");
      }
    }

    this.delegate.visitMethodInsn(INVOKESPECIAL, originalClass, INIT_NAME, this.methodDescriptor, false);
    
    // store engine in field    
    this.delegate.visitVarInsn(ALOAD, 1);
    this.delegate.visitTypeInsn(CHECKCAST, MOXY_ASM_ENGINE_INTERNAL_NAME);    
    this.delegate.visitFieldInsn(PUTFIELD, this.generatingClass, SUPPORT_ENGINE_FIELD_NAME, MOXY_ASM_ENGINE_DESCRIPTOR);
    
    // create and store return and throws maps
    this.delegate.visitTypeInsn(NEW, HASHMAP_INTERNAL_NAME);
    this.delegate.visitInsn(DUP);
    this.delegate.visitMethodInsn(INVOKESPECIAL, HASHMAP_INTERNAL_NAME, INIT_NAME, VOID_VOID_DESCRIPTOR, false);
    this.delegate.visitFieldInsn(PUTFIELD, this.generatingClass, SUPPORT_RETURNMAP_FIELD_NAME, HASHMAP_DESCRIPTOR);
    
    this.delegate.visitTypeInsn(NEW, HASHMAP_INTERNAL_NAME);
    this.delegate.visitInsn(DUP);
    this.delegate.visitMethodInsn(INVOKESPECIAL, HASHMAP_INTERNAL_NAME, INIT_NAME, VOID_VOID_DESCRIPTOR, false);
    this.delegate.visitFieldInsn(PUTFIELD, this.generatingClass, SUPPORT_THROWMAP_FIELD_NAME, HASHMAP_DESCRIPTOR);
    
    this.delegate.visitInsn(RETURN);
  }
}
