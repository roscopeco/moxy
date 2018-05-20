package com.roscopeco.moxy;

import java.io.PrintStream;
import java.util.function.Supplier;

import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.internal.ASMMoxyEngine;

public final class Moxy {
  private static MoxyEngine moxyEngine;
  
  private static MoxyEngine ensureMoxyEngine() {
    if (moxyEngine == null) {
      moxyEngine = new ASMMoxyEngine();
    }
    
    return moxyEngine;
  }
  
  // TODO maybe make this so it throws if engine already set?
  public static void setMoxyEngine(MoxyEngine moxyEngine) {
    Moxy.moxyEngine = moxyEngine;
  }
  
  public static MoxyEngine getMoxyEngine() {
    return Moxy.ensureMoxyEngine();
  }
  
  public static <T> T mock(Class<T> clz) {
    return mock(clz, null);
  }
  
  public static <T> T mock(Class<T> clz, PrintStream trace) {
    return mock(ensureMoxyEngine(), clz, trace);
  }

  public static <T> T mock(MoxyEngine engine, Class<T> clz, PrintStream trace) {
    if (clz == null) {
      throw new IllegalArgumentException("Cannot mock null");
    }
    
    if (engine == null) {
      throw new IllegalArgumentException("Cannot mock with null engine");      
    }
    
    return engine.mock(clz, trace);
  }
  
  public static boolean isMock(Class<?> clz) {
    return isMock(ensureMoxyEngine(), clz);
  }
  
  public static boolean isMock(Object obj) {
    return isMock(ensureMoxyEngine(), obj);
  }  

  public static boolean isMock(MoxyEngine engine, Class<?> clz) {
    return engine.isMock(clz);
  }
  
  public static boolean isMock(MoxyEngine engine, Object obj) {
    return engine.isMock(obj);
  }
  
  /**
   * @deprecated
   * @param invocation
   * @return
   */
  public static <T> MoxyStubber<T> when(T invocation) {
    return when(ensureMoxyEngine(), invocation);
  }
  
  public static <T> MoxyStubber<T> when(Supplier<T> invocation) {
    return when(ensureMoxyEngine(), invocation);
  }

  /**
   * @deprecated
   * @param engine
   * @param invocation
   * @return
   */
  public static <T> MoxyStubber<T> when(MoxyEngine engine, T invocation) {
    return engine.when(invocation);
  }
  
  public static <T> MoxyStubber<T> when(MoxyEngine engine, Supplier<T> invocation) {
    return engine.when(invocation);
  }
  
  /**
   * @deprecated
   * @param invocation
   * @return
   */
  public static MoxyVerifier assertMock(Object invocation) {
    return assertMock(ensureMoxyEngine(), invocation);
  }
  
  public static MoxyVerifier assertMock(Runnable invocation) {
    return assertMock(ensureMoxyEngine(), invocation);
  }
  
  /**
   * @deprecated
   * @param engine
   * @param invocation
   * @return
   */
  public static MoxyVerifier assertMock(MoxyEngine engine, Object invocation) {
    return engine.assertMock(invocation);
  }
  
  public static MoxyVerifier assertMock(MoxyEngine engine, Runnable invocation) {
    return engine.assertMock(invocation);
  }
}
