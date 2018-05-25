package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.MoxyEngine;

// TODO tidy this up
public final class TypesAndDescriptors {
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  /* General Java stuff */
  public static final String OBJECT_INTERNAL_NAME = Type.getInternalName(Object.class);
  public static final String ARRAYLIST_INTERNAL_NAME = Type.getInternalName(ArrayList.class);
  public static final String HASHMAP_INTERNAL_NAME = Type.getInternalName(HashMap.class);
  public static final String THROWABLE_DESCRIPTOR = "L" + Type.getInternalName(Throwable.class) +";";
  public static final String HASHMAP_DESCRIPTOR = "L" + HASHMAP_INTERNAL_NAME + ";";
  public static final String INIT_NAME = "<init>";
  public static final String ADD_NAME = "add";
  public static final String VOID_TYPE = "V";
  public static final String VOID_VOID_DESCRIPTOR = "()V";
  public static final String VOID_INT_DESCRIPTOR = "(I)V";
  public static final String VOID_OBJECT_DESCRIPTOR = "()L" + OBJECT_INTERNAL_NAME + ";";
  public static final String VOID_THROWABLE_DESCRIPTOR = "()" + THROWABLE_DESCRIPTOR;
  public static final String BOOLEAN_OBJECT_DESCRIPTOR = "(L" + OBJECT_INTERNAL_NAME + ";)Z";
  
  /* Moxy stuff */
  public static final String MOXY_ENGINE_DESCRIPTOR = "L" + Type.getInternalName(MoxyEngine.class) + ";";
  public static final String MOXY_ASM_ENGINE_DESCRIPTOR = "L" + Type.getInternalName(ASMMoxyEngine.class) + ";";
  public static final String MOCK_CONSTRUCTOR_DESCRIPTOR = "(" + MOXY_ASM_ENGINE_DESCRIPTOR + ")V";
  
  public static final String MOXY_SUPPORT_INTERFACE_INTERNAL_NAME = Type.getInternalName(ASMMockSupport.class);
  public static final String MOXY_ENGINE_INTERNAL_NAME = Type.getInternalName(MoxyEngine.class);

  /* Recorder */
  public static final String MOXY_RECORDER_INTERNAL_NAME = Type.getInternalName(ThreadLocalInvocationRecorder.class);
  public static final String MOXY_RECORDER_RECORD_METHOD_NAME = "recordInvocation";
  public static final String MOXY_RECORDER_RECORD_DESCRIPTOR = "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V";
  
  /* ASMMoxyMockSupport-related */
  public static final String SUPPORT_GETENGINE_METHOD_NAME = "__moxy_asm_getEngine";
  public static final String SUPPORT_GETENGINE_DESCRIPTOR = "()" + MOXY_ASM_ENGINE_DESCRIPTOR;
  public static final String SUPPORT_GETRETURNMAP_METHOD_NAME = "__moxy_asm_getReturnMap";
  public static final String SUPPORT_GETRETURNMAP_DESCRIPTOR = "()" + HASHMAP_DESCRIPTOR;
  public static final String SUPPORT_GETTHROWMAP_METHOD_NAME = "__moxy_asm_getThrowMap";
  public static final String SUPPORT_GETTHROWMAP_DESCRIPTOR = "()" + HASHMAP_DESCRIPTOR;
  public static final String SUPPORT_GETCURRENTTHROW_METHOD_NAME = "__moxy_asm_getThrowForCurrentInvocation";
  public static final String SUPPORT_GETCURRENTRETURN_METHOD_NAME = "__moxy_asm_getReturnForCurrentInvocation";
  public static final String SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_NAME = "__moxy_asm_throwNullConstructorException";
  public static final String SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_DESCRIPTOR = "()V";
  public static final String SUPPORT_GETRECORDER_METHOD_NAME = "__moxy_asm_getRecorder";
  public static final String SUPPORT_GETRECORDER_DESCRIPTOR = "()L" + MOXY_RECORDER_INTERNAL_NAME + ";";
  
  public static final String SUPPORT_ENGINE_FIELD_NAME = "__moxy_asm_engine";
  public static final String SUPPORT_RETURNMAP_FIELD_NAME = "__moxy_asm_returnMap";
  public static final String SUPPORT_THROWMAP_FIELD_NAME = "__moxy_asm_throwMap";
  
  /* primitives and their corresponding box types */
  public static final String VALUEOF_METHOD_NAME = "valueOf";
  
  public static final char BYTE_PRIMITIVE_INTERNAL_NAME = 'B';
  public static final String BYTE_CLASS_INTERNAL_NAME = Type.getInternalName(Byte.class);
  public static final String BYTE_VALUEOF_DESCRIPTOR = "(B)Ljava/lang/Byte;";
  public static final String BYTEVALUE_METHOD_NAME = "byteValue";
  public static final String BYTEVALUE_DESCRIPTOR = "()B";

  public static final char CHAR_PRIMITIVE_INTERNAL_NAME = 'C';
  public static final String CHAR_CLASS_INTERNAL_NAME = Type.getInternalName(Character.class);
  public static final String CHAR_VALUEOF_DESCRIPTOR = "(C)Ljava/lang/Character;";
  public static final String CHARVALUE_METHOD_NAME = "charValue";
  public static final String CHARVALUE_DESCRIPTOR = "()C";

  public static final char SHORT_PRIMITIVE_INTERNAL_NAME = 'S';
  public static final String SHORT_CLASS_INTERNAL_NAME = Type.getInternalName(Short.class);
  public static final String SHORT_VALUEOF_DESCRIPTOR = "(S)Ljava/lang/Short;";
  public static final String SHORTVALUE_METHOD_NAME = "shortValue";
  public static final String SHORTVALUE_DESCRIPTOR = "()S";

  public static final char INT_PRIMITIVE_INTERNAL_NAME = 'I';
  public static final String INT_CLASS_INTERNAL_NAME = Type.getInternalName(Integer.class);
  public static final String INT_VALUEOF_DESCRIPTOR = "(I)Ljava/lang/Integer;";
  public static final String INTVALUE_METHOD_NAME = "intValue";
  public static final String INTVALUE_DESCRIPTOR = "()I";
  
  public static final char LONG_PRIMITIVE_INTERNAL_NAME = 'J';
  public static final String LONG_CLASS_INTERNAL_NAME = Type.getInternalName(Long.class);
  public static final String LONG_VALUEOF_DESCRIPTOR = "(J)Ljava/lang/Long;";
  public static final String LONGVALUE_METHOD_NAME = "longValue";
  public static final String LONGVALUE_DESCRIPTOR = "()J";

  public static final char FLOAT_PRIMITIVE_INTERNAL_NAME = 'F';
  public static final String FLOAT_CLASS_INTERNAL_NAME = Type.getInternalName(Float.class);
  public static final String FLOAT_VALUEOF_DESCRIPTOR = "(F)Ljava/lang/Float;";
  public static final String FLOATVALUE_METHOD_NAME = "floatValue";
  public static final String FLOATVALUE_DESCRIPTOR = "()F";

  public static final char DOUBLE_PRIMITIVE_INTERNAL_NAME = 'D';
  public static final String DOUBLE_CLASS_INTERNAL_NAME = Type.getInternalName(Double.class);
  public static final String DOUBLE_VALUEOF_DESCRIPTOR = "(D)Ljava/lang/Double;";
  public static final String DOUBLEVALUE_METHOD_NAME = "doubleValue";
  public static final String DOUBLEVALUE_DESCRIPTOR = "()D";

  public static final char BOOL_PRIMITIVE_INTERNAL_NAME = 'Z';
  public static final String BOOL_CLASS_INTERNAL_NAME = Type.getInternalName(Boolean.class);
  public static final String BOOL_VALUEOF_DESCRIPTOR = "(Z)Ljava/lang/Boolean;";
  public static final String BOOLVALUE_METHOD_NAME = "booleanValue";
  public static final String BOOLVALUE_DESCRIPTOR = "()Z";
  
  public static final char VOID_PRIMITIVE_INTERNAL_NAME = 'V';
  public static final String VOID_CLASS_INTERNAL_NAME = Type.getInternalName(Void.class);
  
  public static final char OBJECT_PRIMITIVE_INTERNAL_NAME = 'L';
}