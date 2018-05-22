package com.roscopeco.moxy.impl.asm.visitors;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
    super(ASM6);
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
    
    // Loadconst method name and sig as second and third param
    this.mv.visitLdcInsn(this.methodName);
    this.mv.visitLdcInsn(this.methodDescriptor);

    // Create array as fourth param
    // TODO - could optimise here, no need to create new list if argc == 0,
    //        could instead use static EMPTY_OBJECT_LIST.
    this.mv.visitTypeInsn(NEW, ARRAYLIST_INTERNAL_NAME);
    this.mv.visitInsn(DUP);
    this.mv.visitIntInsn(BIPUSH, argc);
    this.mv.visitMethodInsn(INVOKESPECIAL, ARRAYLIST_INTERNAL_NAME, INIT_NAME, VOID_INT_DESCRIPTOR, false);    
    
    // Go through arguments, load and autobox (if necessary).
    int currentLocalSlot = 1;
    for (int argNum = 0; argNum < argc; argNum++) {
      
      this.mv.visitInsn(DUP);
      
      currentLocalSlot += generateLoadAndAutoboxing(argNum, currentLocalSlot);
      
      this.mv.visitMethodInsn(INVOKEVIRTUAL, ARRAYLIST_INTERNAL_NAME, ADD_NAME, BOOLEAN_OBJECT_DESCRIPTOR, false);
      this.mv.visitInsn(POP);
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
    final Label returnLabel = new Label();
    
    // Always do exception first. Should never have both anyway, this is 
    // enforced in ASMMoxyMockSupport when the fields are set.
    this.mv.visitVarInsn(ALOAD, 0);
    this.mv.visitMethodInsn(INVOKEVIRTUAL, this.generatingClass, SUPPORT_GETCURRENTTHROW_METHOD_NAME, VOID_THROWABLE_DESCRIPTOR, false);    
    this.mv.visitJumpInsn(IFNULL, returnLabel);
    
    this.mv.visitVarInsn(ALOAD, 0);
    this.mv.visitMethodInsn(INVOKEVIRTUAL, this.generatingClass, SUPPORT_GETCURRENTTHROW_METHOD_NAME, VOID_THROWABLE_DESCRIPTOR, false);    
    this.mv.visitInsn(ATHROW);
    
    this.mv.visitLabel(returnLabel);
    
    char primitiveReturnType = returnType.charAt(0);
    switch (primitiveReturnType) {
    case BYTE_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(BYTE_CLASS_INTERNAL_NAME, 
                             BYTEVALUE_METHOD_NAME,
                             BYTEVALUE_DESCRIPTOR,
                             IRETURN,
                             ICONST_0);
      break;
    case CHAR_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(CHAR_CLASS_INTERNAL_NAME, 
                             CHARVALUE_METHOD_NAME,
                             CHARVALUE_DESCRIPTOR,
                             IRETURN,
                             ICONST_0);
      break;
    case SHORT_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(SHORT_CLASS_INTERNAL_NAME, 
                             SHORTVALUE_METHOD_NAME,
                             SHORTVALUE_DESCRIPTOR,
                             IRETURN,
                             ICONST_0);
      break;
    case INT_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(INT_CLASS_INTERNAL_NAME, 
                             INTVALUE_METHOD_NAME,
                             INTVALUE_DESCRIPTOR,
                             IRETURN,
                             ICONST_0);
      break;
    case BOOL_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(BOOL_CLASS_INTERNAL_NAME, 
                             BOOLVALUE_METHOD_NAME,
                             BOOLVALUE_DESCRIPTOR,
                             IRETURN,
                             ICONST_0);
      break;
    case LONG_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(LONG_CLASS_INTERNAL_NAME, 
                             LONGVALUE_METHOD_NAME,
                             LONGVALUE_DESCRIPTOR,
                             LRETURN,
                             LCONST_0);
      break;
    case FLOAT_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(FLOAT_CLASS_INTERNAL_NAME, 
                             FLOATVALUE_METHOD_NAME,
                             FLOATVALUE_DESCRIPTOR,
                             FRETURN,
                             FCONST_0);
      break;
    case DOUBLE_PRIMITIVE_INTERNAL_NAME:
      generateUnboxingReturn(DOUBLE_CLASS_INTERNAL_NAME, 
                             DOUBLEVALUE_METHOD_NAME,
                             DOUBLEVALUE_DESCRIPTOR,
                             DRETURN,
                             DCONST_0);
      break;
    case OBJECT_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitVarInsn(ALOAD, 0);
      this.mv.visitMethodInsn(INVOKEVIRTUAL, this.generatingClass, SUPPORT_GETCURRENTRETURN_METHOD_NAME, VOID_OBJECT_DESCRIPTOR, false);
      this.mv.visitTypeInsn(CHECKCAST, this.returnType);
      this.mv.visitInsn(ARETURN);
      break;
    case VOID_PRIMITIVE_INTERNAL_NAME:
      this.mv.visitInsn(RETURN);
      break;
    default:
      throw new IllegalArgumentException("Unrecognised JVM primitive type: '" + returnType + "'.\n"
                                       + "Your JVM must be super-new and improved.\n"
                                       + "To fix, add mysterious new type to switch in MoxyMockingMethodVisitor#visitCode()");
    }    
  }

  /*
   * Generate an auto-unboxing return using the appropriate value from
   * the ASMMoxySupport.__moxy_asm_getReturnForCurrentInvocation method.
   * The defaultValue parameter is expected to generate the appropriate
   * default value bytecode for primitive types if there is no value 
   * returned from that method (i.e. if this method has not been stubbed).
   */
  void generateUnboxingReturn(String boxClass,
                              String valueOfMethod,
                              String valueOfDescriptor,
                              int returnOpcode,
                              int defaultValueOpcode) {
    final Label defaultValueLabel = new Label();

    this.mv.visitVarInsn(ALOAD, 0);
    this.mv.visitMethodInsn(INVOKEVIRTUAL, this.generatingClass, SUPPORT_GETCURRENTRETURN_METHOD_NAME, VOID_OBJECT_DESCRIPTOR, false);    
    this.mv.visitJumpInsn(IFNULL, defaultValueLabel);

    this.mv.visitVarInsn(ALOAD, 0);
    this.mv.visitMethodInsn(INVOKEVIRTUAL, this.generatingClass, SUPPORT_GETCURRENTRETURN_METHOD_NAME, VOID_OBJECT_DESCRIPTOR, false);
    this.mv.visitTypeInsn(CHECKCAST, boxClass);    
    this.mv.visitMethodInsn(INVOKEVIRTUAL, boxClass, valueOfMethod, valueOfDescriptor, false);
    this.mv.visitInsn(returnOpcode);
    
    this.mv.visitLabel(defaultValueLabel);
    this.mv.visitInsn(defaultValueOpcode);
    this.mv.visitInsn(returnOpcode);
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
    