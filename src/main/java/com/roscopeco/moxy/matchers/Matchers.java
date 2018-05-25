package com.roscopeco.moxy.matchers;

import java.util.Arrays;
import java.util.List;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.MoxyEngine;

public class Matchers {
  public static byte anyByte() {
    return anyByte(Moxy.getMoxyEngine());
  }
  
  public static byte anyByte(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Byte>());
    return 0;
  }

  public static char anyChar() {
    return anyChar(Moxy.getMoxyEngine());
  }
  
  public static char anyChar(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Character>());
    return 0;
  }
  
  public static short anyShort() {
    return anyShort(Moxy.getMoxyEngine());
  }
  
  public static short anyShort(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Short>());
    return 0;
  }
    
  public static int anyInt() {
    return anyInt(Moxy.getMoxyEngine());
  }
  
  public static int anyInt(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Integer>());
    return 0;
  }
  
  public static long anyLong() {
    return anyLong(Moxy.getMoxyEngine());
  }
  
  public static long anyLong(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Long>());
    return 0;
  }
  
  public static float anyFloat() {
    return anyFloat(Moxy.getMoxyEngine());
  }
  
  public static float anyFloat(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Float>());
    return 0;
  }
  
  public static double anyDouble() {
    return anyDouble(Moxy.getMoxyEngine());
  }
  
  public static double anyDouble(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Double>());
    return 0;
  }
  
  public static boolean anyBool() {
    return anyBool(Moxy.getMoxyEngine());
  }
  
  public static boolean anyBool(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Boolean>());
    return false;
  }
  
  public static <T> T any() {
    return any(Moxy.getMoxyEngine());
  }
  
  public static <T> T any(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<T>());
    return null;
  }

  /* ************ EQUALS ************** */

  public static byte eqByte(byte value) {
    return eqByte(Moxy.getMoxyEngine(), value);
  }
  
  public static byte eqByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Byte>(value));
    return 0;
  }

  public static char eqChar(char value) {
    return eqChar(Moxy.getMoxyEngine(), value);
  }
  
  public static char eqChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Character>(value));
    return 0;
  }
  
  public static short eqShort(short value) {
    return eqShort(Moxy.getMoxyEngine(), value);
  }
  
  public static short eqShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Short>(value));
    return 0;
  }
    
  public static int eqInt(int value) {
    return eqInt(Moxy.getMoxyEngine(), value);
  }
  
  public static int eqInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Integer>(value));
    return 0;
  }
  
  public static long eqLong(long value) {
    return eqLong(Moxy.getMoxyEngine(), value);
  }
  
  public static long eqLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Long>(value));
    return 0;
  }
  
  public static float eqFloat(float value) {
    return eqFloat(Moxy.getMoxyEngine(), value);
  }
  
  public static float eqFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Float>(value));
    return 0;
  }
  
  public static double eqDouble(double value) {
    return eqDouble(Moxy.getMoxyEngine(), value);
  }
  
  public static double eqDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Double>(value));
    return 0;
  }
  
  public static boolean eqBool(boolean value) {
    return eqBool(Moxy.getMoxyEngine(), value);
  }
  
  public static boolean eqBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Boolean>(value));
    return false;
  }
  
  public static <T> T eq(T value) {
    return eq(Moxy.getMoxyEngine(), value);
  }
  
  public static <T> T eq(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<T>(value));
    return null;
  }
  
  /* ************ NOTEQUALS ************** */

  public static byte neqByte(byte value) {
    return neqByte(Moxy.getMoxyEngine(), value);
  }
  
  public static byte neqByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Byte>(value));
    return 0;
  }

  public static char neqChar(char value) {
    return neqChar(Moxy.getMoxyEngine(), value);
  }
  
  public static char neqChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Character>(value));
    return 0;
  }
  
  public static short neqShort(short value) {
    return neqShort(Moxy.getMoxyEngine(), value);
  }
  
  public static short neqShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Short>(value));
    return 0;
  }
    
  public static int neqInt(int value) {
    return neqInt(Moxy.getMoxyEngine(), value);
  }
  
  public static int neqInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Integer>(value));
    return 0;
  }
  
  public static long neqLong(long value) {
    return neqLong(Moxy.getMoxyEngine(), value);
  }
  
  public static long neqLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Long>(value));
    return 0;
  }
  
  public static float neqFloat(float value) {
    return neqFloat(Moxy.getMoxyEngine(), value);
  }
  
  public static float neqFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Float>(value));
    return 0;
  }
  
  public static double neqDouble(double value) {
    return neqDouble(Moxy.getMoxyEngine(), value);
  }
  
  public static double neqDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Double>(value));
    return 0;
  }
  
  public static boolean neqBool(boolean value) {
    return neqBool(Moxy.getMoxyEngine(), value);
  }
  
  public static boolean neqBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Boolean>(value));
    return false;
  }
  
  public static <T> T neq(T value) {
    return neq(Moxy.getMoxyEngine(), value);
  }
  
  public static <T> T neq(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<T>(value));
    return null;
  }
  
  /* ************ LESSTHAN ************** */

  public static byte ltByte(byte value) {
    return ltByte(Moxy.getMoxyEngine(), value);
  }
  
  public static byte ltByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Byte>(value));
    return 0;
  }

  public static char ltChar(char value) {
    return ltChar(Moxy.getMoxyEngine(), value);
  }
  
  public static char ltChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Character>(value));
    return 0;
  }
  
  public static short ltShort(short value) {
    return ltShort(Moxy.getMoxyEngine(), value);
  }
  
  public static short ltShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Short>(value));
    return 0;
  }
    
  public static int ltInt(int value) {
    return ltInt(Moxy.getMoxyEngine(), value);
  }
  
  public static int ltInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Integer>(value));
    return 0;
  }
  
  public static long ltLong(long value) {
    return ltLong(Moxy.getMoxyEngine(), value);
  }
  
  public static long ltLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Long>(value));
    return 0;
  }
  
  public static float ltFloat(float value) {
    return ltFloat(Moxy.getMoxyEngine(), value);
  }
  
  public static float ltFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Float>(value));
    return 0;
  }
  
  public static double ltDouble(double value) {
    return ltDouble(Moxy.getMoxyEngine(), value);
  }
  
  public static double ltDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Double>(value));
    return 0;
  }
  
  public static boolean ltBool(boolean value) {
    return ltBool(Moxy.getMoxyEngine(), value);
  }
  
  public static boolean ltBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Boolean>(value));
    return false;
  }
  
  public static <T extends Comparable<T>> T lt(T value) {
    return lt(Moxy.getMoxyEngine(), value);
  }
  
  public static <T extends Comparable<T>> T lt(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<T>(value));
    return null;
  }
  
  /* ************ GREATERTHAN ************** */

  public static byte gtByte(byte value) {
    return gtByte(Moxy.getMoxyEngine(), value);
  }
  
  public static byte gtByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Byte>(value));
    return 0;
  }

  public static char gtChar(char value) {
    return gtChar(Moxy.getMoxyEngine(), value);
  }
  
  public static char gtChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Character>(value));
    return 0;
  }
  
  public static short gtShort(short value) {
    return gtShort(Moxy.getMoxyEngine(), value);
  }
  
  public static short gtShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Short>(value));
    return 0;
  }
    
  public static int gtInt(int value) {
    return gtInt(Moxy.getMoxyEngine(), value);
  }
  
  public static int gtInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Integer>(value));
    return 0;
  }
  
  public static long gtLong(long value) {
    return gtLong(Moxy.getMoxyEngine(), value);
  }
  
  public static long gtLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Long>(value));
    return 0;
  }
  
  public static float gtFloat(float value) {
    return gtFloat(Moxy.getMoxyEngine(), value);
  }
  
  public static float gtFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Float>(value));
    return 0;
  }
  
  public static double gtDouble(double value) {
    return gtDouble(Moxy.getMoxyEngine(), value);
  }
  
  public static double gtDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Double>(value));
    return 0;
  }
  
  public static boolean gtBool(boolean value) {
    return gtBool(Moxy.getMoxyEngine(), value);
  }
  
  public static boolean gtBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Boolean>(value));
    return false;
  }
  
  public static <T extends Comparable<T>> T gt(T value) {
    return gt(Moxy.getMoxyEngine(), value);
  }
  
  public static <T extends Comparable<T>> T gt(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<T>(value));
    return null;
  }
  
  /* ************ ANYOF ************** */

  public static byte anyOfByte(Byte... possibilities) {
    return anyOfByte(Arrays.asList(possibilities));
  }
  
  public static byte anyOfByte(List<Byte> possibilities) {
    return anyOfByte(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static byte anyOfByte(MoxyEngine engine, List<Byte> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Byte>(possibilities));
    return 0;
  }
  
  public static char anyOfChar(Character... possibilities) {
    return anyOfChar(Arrays.asList(possibilities));
  }

  public static char anyOfChar(List<Character> possibilities) {
    return anyOfChar(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static char anyOfChar(MoxyEngine engine, List<Character> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Character>(possibilities));
    return 0;
  }
  
  public static short anyOfShort(Short... possibilities) {
    return anyOfShort(Arrays.asList(possibilities));
  }
  
  public static short anyOfShort(List<Short> possibilities) {
    return anyOfShort(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static short anyOfShort(MoxyEngine engine, List<Short> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Short>(possibilities));
    return 0;
  }
    
  public static int anyOfInt(Integer... possibilities) {
    return anyOfInt(Arrays.asList(possibilities));
  }
  
  public static int anyOfInt(List<Integer> possibilities) {
    return anyOfInt(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static int anyOfInt(MoxyEngine engine, List<Integer> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Integer>(possibilities));
    return 0;
  }
  
  public static long anyOfLong(Long... possibilities) {
    return anyOfLong(Arrays.asList(possibilities));
  }
  
  public static long anyOfLong(List<Long> possibilities) {
    return anyOfLong(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static long anyOfLong(MoxyEngine engine, List<Long> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Long>(possibilities));
    return 0l;
  }
  
  public static float anyOfFloat(Float... possibilities) {
    return anyOfFloat(Arrays.asList(possibilities));
  }
  
  public static float anyOfFloat(List<Float> possibilities) {
    return anyOfFloat(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static float anyOfFloat(MoxyEngine engine, List<Float> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Float>(possibilities));
    return 0.0f;
  }
  
  public static double anyOfDouble(Double... possibilities) {
    return anyOfDouble(Arrays.asList(possibilities));
  }
  
  public static double anyOfDouble(List<Double> possibilities) {
    return anyOfDouble(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static double anyOfDouble(MoxyEngine engine, List<Double> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Double>(possibilities));
    return 0.0d;
  }
  
  public static boolean anyOfBool(Boolean... possibilities) {
    return anyOfBool(Arrays.asList(possibilities));
  }
  
  public static boolean anyOfBool(List<Boolean> possibilities) {
    return anyOfBool(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static boolean anyOfBool(MoxyEngine engine, List<Boolean> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Boolean>(possibilities));
    return false;
  }
  
  @SafeVarargs
  public static <T> T anyOf(T... possibilities) {
    return anyOf(Arrays.asList(possibilities));
  }
  
  public static <T> T anyOf(List<? extends T> possibilities) {
    return anyOf(Moxy.getMoxyEngine(), possibilities);
  }
  
  public static <T> T anyOf(MoxyEngine engine, List<? extends T> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<T>(possibilities));
    return null;
  }

  /* ************ AND ************** */

  public static byte andByte(Byte... fromMatchers) {
    return andByte(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static byte andByte(MoxyEngine engine, Byte... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Byte>(fromMatchers));
    return 0;
  }

  public static char andChar(Character... fromMatchers) {
    return andChar(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static char andChar(MoxyEngine engine, Character... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Character>(fromMatchers));
    return 0;
  }
  
  public static short andShort(Short... fromMatchers) {
    return andShort(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static short andShort(MoxyEngine engine, Short... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Short>(fromMatchers));
    return 0;
  }
    
  public static int andInt(Integer... fromMatchers) {
    return andInt(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static int andInt(MoxyEngine engine, Integer... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Integer>(fromMatchers));
    return 0;
  }
  
  public static long andLong(Long... fromMatchers) {
    return andLong(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static long andLong(MoxyEngine engine, Long... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Long>(fromMatchers));
    return 0;
  }
  
  public static float andFloat(Float... fromMatchers) {
    return andFloat(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static float andFloat(MoxyEngine engine, Float... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Float>(fromMatchers));
    return 0;
  }
  
  public static double andDouble(Double... fromMatchers) {
    return andDouble(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static double andDouble(MoxyEngine engine, Double... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Double>(fromMatchers));
    return 0;
  }
  
  public static boolean andBool(Boolean... fromMatchers) {
    return andBool(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  public static boolean andBool(MoxyEngine engine, Boolean... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Boolean>(fromMatchers));
    return false;
  }
  
  @SafeVarargs
  public static <T> T and(T... fromMatchers) {
    return and(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  @SafeVarargs
  public static <T> T and(MoxyEngine engine, T... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<T>(fromMatchers));
    return null;
  }

  /* ************ NOT ************** */

  public static byte notByte(byte fromMatcher) {
    return notByte(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static byte notByte(MoxyEngine engine, byte fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Byte>());
    return 0;
  }

  public static char notChar(char fromMatcher) {
    return notChar(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static char notChar(MoxyEngine engine, char fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Character>());
    return 0;
  }
  
  public static short notShort(short fromMatcher) {
    return notShort(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static short notShort(MoxyEngine engine, short fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Short>());
    return 0;
  }
    
  public static int notInt(int fromMatcher) {
    return notInt(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static int notInt(MoxyEngine engine, int fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Integer>());
    return 0;
  }
  
  public static long notLong(long fromMatcher) {
    return notLong(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static long notLong(MoxyEngine engine, long fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Long>());
    return 0;
  }
  
  public static float notFloat(float fromMatcher) {
    return notFloat(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static float notFloat(MoxyEngine engine, float fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Float>());
    return 0;
  }
  
  public static double notDouble(double fromMatcher) {
    return notDouble(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static double notDouble(MoxyEngine engine, double fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Double>());
    return 0;
  }
  
  public static boolean notBool(boolean fromMatcher) {
    return notBool(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static boolean notBool(MoxyEngine engine, boolean fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Boolean>());
    return false;
  }
  
  public static <T> T not(T fromMatcher) {
    return not(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  public static <T> T not(MoxyEngine engine, T fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<T>());
    return null;
  }
}
