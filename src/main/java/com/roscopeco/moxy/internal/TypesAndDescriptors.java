package com.roscopeco.moxy.internal;

import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyInvocationRecorder;

public final class TypesAndDescriptors {
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  /* General Java stuff */
  public static final String OBJECT_INTERNAL_NAME = Type.getInternalName(Object.class);
  public static final String THROWABLE_DESCRIPTOR = "L" + Type.getInternalName(Throwable.class) +";";
  public static final String INIT_NAME = "<init>";
  public static final String VOID_TYPE = "V";
  public static final String VOID_VOID_DESCRIPTOR = "()V";
  
  /* Moxy stuff */
  public static final String MOXY_ENGINE_DESCRIPTOR = "L" + Type.getInternalName(MoxyEngine.class) + ";";
  public static final String MOXY_ASM_ENGINE_DESCRIPTOR = "L" + Type.getInternalName(ASMMoxyEngine.class) + ";";
  public static final String MOCK_CONSTRUCTOR_DESCRIPTOR = "(" + MOXY_ASM_ENGINE_DESCRIPTOR + ")V";
  
  public static final String MOXY_SUPPORT_INTERFACE_INTERNAL_NAME = Type.getInternalName(ASMMockSupport.class);
  public static final String MOXY_ENGINE_INTERNAL_NAME = Type.getInternalName(MoxyEngine.class);

  /* Recorder */
  public static final String MOXY_RECORDER_INTERNAL_NAME = Type.getInternalName(MoxyInvocationRecorder.class);
  public static final String MOXY_RECORDER_RECORD_METHOD_NAME = "recordInvocation";
  public static final String MOXY_RECORDER_RECORD_DESCRIPTOR = "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V";
  
  /* ASMMoxyMockSupport-related */
  public static final String SUPPORT_GETENGINE_METHOD_NAME = "__moxy_asm_getEngine";
  public static final String SUPPORT_GETENGINE_DESCRIPTOR = "()L" + MOXY_ENGINE_INTERNAL_NAME + ";";
  public static final String SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_NAME = "__moxy_asm_throwNullConstructorException";
  public static final String SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_DESCRIPTOR = "()V";
  public static final String SUPPORT_GETRECORDER_METHOD_NAME = "__moxy_asm_getRecorder";
  public static final String SUPPORT_GETRECORDER_DESCRIPTOR = "()L" + MOXY_RECORDER_INTERNAL_NAME + ";";
  
  public static final String SUPPORT_ENGINE_FIELD_NAME = "__moxy_asm_engine";
  
  /* primitives and their corresponding box types */
  public static final String VALUEOF_METHOD_NAME = "valueOf";
  
  public static final char BYTE_PRIMITIVE_INTERNAL_NAME = 'B';
  public static final String BYTE_CLASS_INTERNAL_NAME = Type.getInternalName(Byte.class);
  public static final String BYTE_VALUEOF_DESCRIPTOR = "(B)Ljava/lang/Byte;";

  public static final char CHAR_PRIMITIVE_INTERNAL_NAME = 'C';
  public static final String CHAR_CLASS_INTERNAL_NAME = Type.getInternalName(Character.class);
  public static final String CHAR_VALUEOF_DESCRIPTOR = "(C)Ljava/lang/Character;";

  public static final char SHORT_PRIMITIVE_INTERNAL_NAME = 'S';
  public static final String SHORT_CLASS_INTERNAL_NAME = Type.getInternalName(Short.class);
  public static final String SHORT_VALUEOF_DESCRIPTOR = "(S)Ljava/lang/Short;";
  
  public static final char INT_PRIMITIVE_INTERNAL_NAME = 'I';
  public static final String INT_CLASS_INTERNAL_NAME = Type.getInternalName(Integer.class);
  public static final String INT_VALUEOF_DESCRIPTOR = "(I)Ljava/lang/Integer;";
  
  public static final char LONG_PRIMITIVE_INTERNAL_NAME = 'J';
  public static final String LONG_CLASS_INTERNAL_NAME = Type.getInternalName(Long.class);
  public static final String LONG_VALUEOF_DESCRIPTOR = "(J)Ljava/lang/Long;";

  public static final char FLOAT_PRIMITIVE_INTERNAL_NAME = 'F';
  public static final String FLOAT_CLASS_INTERNAL_NAME = Type.getInternalName(Float.class);
  public static final String FLOAT_VALUEOF_DESCRIPTOR = "(F)Ljava/lang/Float;";

  public static final char DOUBLE_PRIMITIVE_INTERNAL_NAME = 'D';
  public static final String DOUBLE_CLASS_INTERNAL_NAME = Type.getInternalName(Double.class);
  public static final String DOUBLE_VALUEOF_DESCRIPTOR = "(D)Ljava/lang/Double;";

  public static final char BOOL_PRIMITIVE_INTERNAL_NAME = 'Z';
  public static final String BOOL_CLASS_INTERNAL_NAME = Type.getInternalName(Boolean.class);
  public static final String BOOL_VALUEOF_DESCRIPTOR = "(Z)Ljava/lang/Boolean;";
  
  public static final char VOID_PRIMITIVE_INTERNAL_NAME = 'V';
  public static final String VOID_CLASS_INTERNAL_NAME = Type.getInternalName(Void.class);
  
  public static final char OBJECT_PRIMITIVE_INTERNAL_NAME = 'L';

  public static String sanitiseTypeNameForMemberName(final String descriptor) {
    return descriptor.replaceAll("[./;]", "");
  }
  
  public static String makeMethodReturnFieldName(final String name, final String desc) {
    return "__moxy_asm_return_" + name + sanitiseTypeNameForMemberName(desc);
  }

  public static String makeMethodThrowFieldName(final String name, final String desc) {
    return "__moxy_asm_throw_" + name + sanitiseTypeNameForMemberName(desc);
  }


}
