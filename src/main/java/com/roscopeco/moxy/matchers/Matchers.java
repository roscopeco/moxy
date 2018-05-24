package com.roscopeco.moxy.matchers;

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
  
  public static Object any() {
    return any(Moxy.getMoxyEngine());
  }
  
  public static Object any(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Object>());
    return null;
  }
}
