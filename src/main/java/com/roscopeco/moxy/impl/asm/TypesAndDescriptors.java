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
package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.InvalidStubbingException;
import com.roscopeco.moxy.api.MoxyEngine;

/**
 * Holds internal types and descriptors for the
 * {@link com.roscopeco.moxy.impl.asm.ASMMoxyEngine} and supporting code.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
// TODO tidy this up
public final class TypesAndDescriptors {
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

  /* General Java stuff */
  public static final String OBJECT_INTERNAL_NAME = Type.getInternalName(Object.class);
  public static final String OBJECT_DESCRIPTOR = "L" + OBJECT_INTERNAL_NAME + ";";
  public static final String ARRAYLIST_INTERNAL_NAME = Type.getInternalName(ArrayList.class);
  public static final String THROWABLE_INTERNAL_NAME = Type.getInternalName(Throwable.class);
  public static final String THROWABLE_DESCRIPTOR = "L" + THROWABLE_INTERNAL_NAME +";";
  public static final String MAP_INTERNAL_NAME = Type.getInternalName(Map.class);
  public static final String MAP_DESCRIPTOR = "L" + MAP_INTERNAL_NAME + ";";
  public static final String HASHMAP_INTERNAL_NAME = Type.getInternalName(HashMap.class);
  public static final String HASHMAP_DESCRIPTOR = "L" + HASHMAP_INTERNAL_NAME + ";";
  public static final String STRING_INTERNAL_NAME = Type.getInternalName(String.class);
  public static final String STRING_DESCRIPTOR = "L" + STRING_INTERNAL_NAME + ";";
  public static final String INIT_NAME = "<init>";
  public static final String ADD_NAME = "add";
  public static final String VOID_TYPE = "V";
  public static final String VOID_VOID_DESCRIPTOR = "()V";
  public static final String BOOLEAN_VOID_DESCRIPTOR = "()Z";
  public static final String VOID_INT_DESCRIPTOR = "(I)V";
  public static final String OBJECT_VOID_DESCRIPTOR = "()" + OBJECT_DESCRIPTOR;
  public static final String THROWABLE_VOID_DESCRIPTOR = "()" + THROWABLE_DESCRIPTOR;
  public static final String MAP_VOID_DESCRIPTOR = "()" + MAP_DESCRIPTOR;
  public static final String VOID_STRING_DESCRIPTOR = "(" + STRING_DESCRIPTOR + ")V";
  public static final String VOID_STRING_STRING_DESCRIPTOR = "(" + STRING_DESCRIPTOR + STRING_DESCRIPTOR + ")V";
  public static final String BOOLEAN_OBJECT_DESCRIPTOR = "(L" + OBJECT_INTERNAL_NAME + ";)Z";
  public static final String VOID_OBJECT_THROWABLE_DESCRIPTOR = "(" + OBJECT_DESCRIPTOR + THROWABLE_DESCRIPTOR + ")V";

  /* Moxy stuff */
  public static final String MOXY_ENGINE_DESCRIPTOR = "L" + Type.getInternalName(MoxyEngine.class) + ";";
  public static final String MOXY_ASM_ENGINE_INTERNAL_NAME = Type.getInternalName(ASMMoxyEngine.class);
  public static final String MOXY_ASM_ENGINE_DESCRIPTOR = "L" + MOXY_ASM_ENGINE_INTERNAL_NAME + ";";
  public static final String MOCK_CONSTRUCTOR_DESCRIPTOR = "(" + MOXY_ENGINE_DESCRIPTOR + ")V";

  public static final String MOXY_SUPPORT_INTERFACE_INTERNAL_NAME = Type.getInternalName(ASMMockSupport.class);
  public static final String MOXY_SUPPORT_IVARS_INTERNAL_NAME = Type.getInternalName(ASMMockInstanceVars.class);
  public static final String MOXY_SUPPORT_ivars_DESCRIPTOR = "L" + MOXY_SUPPORT_IVARS_INTERNAL_NAME + ";";

  public static final String MOXY_ENGINE_INTERNAL_NAME = Type.getInternalName(MoxyEngine.class);

  public static final String INVALID_STUBBING_INTERNAL_NAME = Type.getInternalName(InvalidStubbingException.class);

  /* Recorder */
  public static final String MOXY_RECORDER_INTERNAL_NAME = Type.getInternalName(ThreadLocalInvocationRecorder.class);
  public static final String MOXY_RECORDER_DESCRIPTOR = "L" + MOXY_RECORDER_INTERNAL_NAME + ";";
  public static final String MOXY_RECORDER_RECORD_METHOD_NAME = "recordInvocation";
  public static final String MOXY_RECORDER_RECORD_DESCRIPTOR = "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V";

  /* ASMMoxyMockSupport-related */
  public static final String SUPPORT_GETENGINE_METHOD_NAME = "__moxy_asm_getEngine";
  public static final String SUPPORT_GETENGINE_DESCRIPTOR = "()" + MOXY_ASM_ENGINE_DESCRIPTOR;
  public static final String SUPPORT_GETRETURNMAP_METHOD_NAME = "__moxy_asm_getReturnMap";
  public static final String SUPPORT_GETRETURNMAP_DESCRIPTOR = MAP_VOID_DESCRIPTOR;
  public static final String SUPPORT_GETTHROWMAP_METHOD_NAME = "__moxy_asm_getThrowMap";
  public static final String SUPPORT_GETTHROWMAP_DESCRIPTOR = MAP_VOID_DESCRIPTOR;
  public static final String SUPPORT_GETSUPERMAP_METHOD_NAME = "__moxy_asm_getCallSuperMap";
  public static final String SUPPORT_GETSUPERMAP_DESCRIPTOR = MAP_VOID_DESCRIPTOR;
  public static final String SUPPORT_GETDOACTIONSMAP_METHOD_NAME = "__moxy_asm_getDoActionsMap";
  public static final String SUPPORT_GETDOACTIONSMAP_DESCRIPTOR = MAP_VOID_DESCRIPTOR;
  public static final String SUPPORT_GETCURRENTTHROW_METHOD_NAME = "__moxy_asm_getThrowForCurrentInvocation";
  public static final String SUPPORT_GETCURRENTTHROW_DESCRIPTOR = THROWABLE_VOID_DESCRIPTOR;
  public static final String SUPPORT_GETCURRENTRETURN_METHOD_NAME = "__moxy_asm_getReturnForCurrentInvocation";
  public static final String SUPPORT_GETCURRENTRETURN_DESCRIPTOR = OBJECT_VOID_DESCRIPTOR;
  public static final String SUPPORT_SHOULD_CALL_SUPER_METHOD_NAME = "__moxy_asm_shouldCallSuperForCurrentInvocation";
  public static final String SUPPORT_SHOULD_CALL_SUPER_DESCRIPTOR = BOOLEAN_VOID_DESCRIPTOR;
  public static final String SUPPORT_RUN_DOACTIONS_METHOD_NAME = "__moxy_asm_runDoActionsForCurrentInvocation";
  public static final String SUPPORT_RUN_DOACTIONS_METHOD_DESCRIPTOR = VOID_VOID_DESCRIPTOR;
  public static final String SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_NAME = "__moxy_asm_throwNullConstructorException";
  public static final String SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_DESCRIPTOR = VOID_VOID_DESCRIPTOR;
  public static final String SUPPORT_GETRECORDER_METHOD_NAME = "__moxy_asm_getRecorder";
  public static final String SUPPORT_GETRECORDER_DESCRIPTOR = "()" + MOXY_RECORDER_DESCRIPTOR;
  public static final String SUPPORT_UPDATECURRENTRETURNED_METHOD_NAME = "__moxy_asm_updateCurrentInvocationReturnThrow";
  public static final String SUPPORT_UPDATECURRENTRETURNED_DESCRIPTOR = VOID_OBJECT_THROWABLE_DESCRIPTOR;
  public static final String SUPPORT_MAKE_JAVA_SIGNATURE_METHOD_NAME = "__moxy_asm_makeJavaSignature";
  public static final String SUPPORT_MAKE_JAVA_SIGNATURE_DESCRIPTOR =
      "(" + STRING_DESCRIPTOR + STRING_DESCRIPTOR + ")" + STRING_DESCRIPTOR;
  public static final String SUPPORT_IS_STUBBING_DISABLED_METHOD_NAME = "__moxy_asm_isMockBehaviourDisabledOnThisThread";
  public static final String SUPPORT_IS_STUBBING_DISABLED_DESCRIPTOR = BOOLEAN_VOID_DESCRIPTOR;
  public static final String SUPPORT_ivars_FIELD_NAME = "__moxy_asm_ivars";
  public static final String SUPPORT_GET_ivars_METHOD_NAME = "__moxy_asm_ivars";
  public static final String SUPPORT_GET_ivars_DESCRIPTOR = "()" + MOXY_SUPPORT_ivars_DESCRIPTOR;
  public static final String SUPPORT_ivars_CTOR_DESCRIPTOR = "(" + MOXY_ASM_ENGINE_DESCRIPTOR + ")V";

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
  public static final String BOOLVALUE_DESCRIPTOR = BOOLEAN_VOID_DESCRIPTOR;

  public static final char VOID_PRIMITIVE_INTERNAL_NAME = 'V';
  public static final String VOID_CLASS_INTERNAL_NAME = Type.getInternalName(Void.class);

  public static final char OBJECT_PRIMITIVE_INTERNAL_NAME = 'L';

  public static final char ARRAY_PRIMITIVE_INTERNAL_NAME = '[';

  private TypesAndDescriptors() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.TypesAndDescriptors is not designed for instantiation");
  }
}
