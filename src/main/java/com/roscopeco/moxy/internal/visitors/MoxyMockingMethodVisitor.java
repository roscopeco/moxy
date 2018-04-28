package com.roscopeco.moxy.internal.visitors;

import static org.objectweb.asm.Opcodes.*;
import static com.roscopeco.moxy.internal.TypesAndDescriptors.*;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.internal.TypesAndDescriptors;

class MoxyMockingMethodVisitor extends MethodVisitor {
  // Hide super field to prevent generation of original code.
  private final MethodVisitor mv;
  private final boolean wasAbstract;
  private final String generatingClass;
  private final String returnType;
  private final Type[] argTypes;
  private final String methodName;
  private final String methodDescriptor;
  
  public MoxyMockingMethodVisitor(final MethodVisitor delegate,
                                  final String generatingClass,
                                  final String methodName,
                                  final String methodDescriptor,
                                  final String returnType, 
                                  final Type[] argTypes, 
                                  final boolean wasAbstract) {
    // don't pass the delegate to the super constructor, or we'll generate
    // both old and new bytecode. Instead, just use local mv field and
    // stash the delegate directly.
    super(ASM5);
    this.mv = delegate;
    this.generatingClass = generatingClass;
    this.returnType = returnType;
    this.methodName = methodName;
    this.methodDescriptor = methodDescriptor;
    this.argTypes = argTypes;
    this.wasAbstract = wasAbstract;    
  }

  @Override
  public void visitCode() {
    this.generatePreamble();
    this.generateReturn();
  }
  
  void generatePreamble() {
    int argc = this.argTypes.length;
    
    // load self to pass to recordInvocation later
    this.mv.visitVarInsn(ALOAD, 0);
    
    // Get recorder from Support interface method as receiver
    this.mv.visitInsn(DUP);
    this.mv.visitMethodInsn(INVOKEINTERFACE, 
                            MOXY_SUPPORT_INTERFACE_INTERNAL_NAME, 
                            SUPPORT_GETRECORDER_METHOD_NAME,
                            SUPPORT_GETRECORDER_DESCRIPTOR,
                            true);
    
    // Make stack right for recorder being receiver, and `this` being first param in later invoke.
    this.mv.visitInsn(SWAP);
    
    // Loadconst method name and sig as second param
    this.mv.visitLdcInsn(this.methodName + this.methodDescriptor);

    // Create array as third param
    // TODO - could optimise here, no need to create new array if argc == 0,
    //        could instead use static EMPTY_OBJECT_ARRAY.
    this.mv.visitIntInsn(BIPUSH, argc);
    this.mv.visitTypeInsn(ANEWARRAY, OBJECT_INTERNAL_NAME);
    
    // Go through arguments, load and autobox (if necessary).
    int currentLocalSlot = 1;
    for (int argNum = 0; argNum < argc; argNum++) {
      
      this.mv.visitInsn(DUP);
      this.mv.visitIntInsn(BIPUSH, argNum);
      
      currentLocalSlot += generateLoadAndAutoboxing(argNum, currentLocalSlot);
      
      this.mv.visitInsn(AASTORE);
    }
    
    // Call recorder
    this.mv.visitMethodInsn(INVOKEINTERFACE, 
                            MOXY_RECORDER_INTERNAL_NAME, 
                            MOXY_RECORDER_RECORD_METHOD_NAME,
                            MOXY_RECORDER_RECORD_DESCRIPTOR,
                            true);
    
  }
  
  /* Little bit of weirdness going on here!
   * 
   * Some types (i.e. long and double) take up two local slots. This method
   * takes care of that by returning the number of slots that were taken
   * up by this particular load. 
   * 
   * The caller should add the result to any iterator variable or whatever
   * they're using to keep track of locals.
   */
  // TODO this could probably be DRYed...
  int generateLoadAndAutoboxing(int argNum, int currentLocalSlot) {
    char argType = this.argTypes[argNum].toString().charAt(0);
    int localSlots = 1;
    switch (argType) {
    case BYTE_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ILOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              BYTE_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              BYTE_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case CHAR_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ILOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              CHAR_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              CHAR_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case SHORT_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ILOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              SHORT_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              SHORT_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case INT_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ILOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              INT_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              INT_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case BOOL_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ILOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              BOOL_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              BOOL_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case LONG_PRIMITIVE_INTERNAL_NAME:
      localSlots = 2;
      this.mv.visitVarInsn(LLOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              LONG_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              LONG_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case FLOAT_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(FLOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              FLOAT_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              FLOAT_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case DOUBLE_PRIMITIVE_INTERNAL_NAME:
      localSlots = 2;
      this.mv.visitVarInsn(DLOAD, currentLocalSlot);
      this.mv.visitMethodInsn(INVOKESTATIC, 
                              DOUBLE_CLASS_INTERNAL_NAME, 
                              VALUEOF_METHOD_NAME, 
                              DOUBLE_VALUEOF_DESCRIPTOR,
                              false);
      break;
    case OBJECT_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ALOAD, currentLocalSlot);
      break;
    default:
      throw new IllegalArgumentException("Unrecognised JVM primitive type: '" + argType + "'.\n"
          + "Your JVM must be super-new and improved.\n"
          + "To fix, add mysterious new type to switch in MoxyMockingMethodVisitor#generateLoadAndAutoboxing()");
    }
    return localSlots;
  }
 
  // TODO this could probably be DRYed
  void generateReturn() {
    char primitiveReturnType = returnType.charAt(0);
    switch (primitiveReturnType) {
    case BYTE_PRIMITIVE_INTERNAL_NAME:
    case CHAR_PRIMITIVE_INTERNAL_NAME:
    case SHORT_PRIMITIVE_INTERNAL_NAME:
    case INT_PRIMITIVE_INTERNAL_NAME:
    case BOOL_PRIMITIVE_INTERNAL_NAME:
      /* byte/char/short/int/bool */
      this.mv.visitVarInsn(ALOAD, 0);
      this.mv.visitFieldInsn(GETFIELD, this.generatingClass, TypesAndDescriptors.makeMethodReturnFieldName(this.methodName, this.methodDescriptor), this.returnType);
      this.mv.visitInsn(IRETURN);
      break;
    case LONG_PRIMITIVE_INTERNAL_NAME:
      /* long */
      this.mv.visitVarInsn(ALOAD, 0);
      this.mv.visitFieldInsn(GETFIELD, this.generatingClass, TypesAndDescriptors.makeMethodReturnFieldName(this.methodName, this.methodDescriptor), this.returnType);
      this.mv.visitInsn(LRETURN);
      break;
    case FLOAT_PRIMITIVE_INTERNAL_NAME:
      /* float */
      this.mv.visitVarInsn(ALOAD, 0);
      this.mv.visitFieldInsn(GETFIELD, this.generatingClass, TypesAndDescriptors.makeMethodReturnFieldName(this.methodName, this.methodDescriptor), this.returnType);
      this.mv.visitInsn(FRETURN);
      break;
    case DOUBLE_PRIMITIVE_INTERNAL_NAME:
      /* double */
      this.mv.visitVarInsn(ALOAD, 0);
      this.mv.visitFieldInsn(GETFIELD, this.generatingClass, TypesAndDescriptors.makeMethodReturnFieldName(this.methodName, this.methodDescriptor), this.returnType);
      this.mv.visitInsn(DRETURN);
      break;
    case OBJECT_PRIMITIVE_INTERNAL_NAME:
      /* Object */
      this.mv.visitVarInsn(ALOAD, 0);
      this.mv.visitFieldInsn(GETFIELD, this.generatingClass, TypesAndDescriptors.makeMethodReturnFieldName(this.methodName, this.methodDescriptor), this.returnType);
      this.mv.visitInsn(ARETURN);
      break;
    case VOID_PRIMITIVE_INTERNAL_NAME:
      /* void */
      this.mv.visitInsn(RETURN);
      break;
    default:
      throw new IllegalArgumentException("Unrecognised JVM primitive type: '" + returnType + "'.\n"
                                       + "Your JVM must be super-new and improved.\n"
                                       + "To fix, add mysterious new type to switch in MoxyMockingMethodVisitor#visitCode()");
    }
  }
  
  @Override
  public void visitEnd() {
    // If the method was abstract, we'll need to manually force
    // code generation (since there's no original code attribute
    // to visit).
    if (this.wasAbstract) {
      this.visitCode();
      super.visitEnd();
    }
  }
}
    