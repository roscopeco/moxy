package com.roscopeco.moxy.api;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

public interface MoxyEngine {

  public MoxyMatcherEngine getMatcherEngine();
  
  /**
   * Reset this engine. Depending on implementation, may only
   * reset for the current thread.
   */
  public void reset();
  
  /**
   * Generates a new mock based on `clz` and then defines it in the given classloader.
   *
   * @param loader The classloader to define the new class in.
   * @param clz The original class.
   * @param methods Methods to mock.
   * @param trace If non-null, the resulting class will be dumped (with a `TraceClassVisitor` to the given stream. 
   * 
   * @return The new `Class` object.
   */
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, Set<Method> methods, PrintStream trace);

  public <T> T mock(Class<T> clz);

  public <T> T mock(Class<T> clz, PrintStream trace);

  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, Set<Method> methods);

  public <I> Class<? extends I> getMockClass(Class<I> clz, Set<Method> extraMethods);

  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz);

  public <I> Class<? extends I> getMockClass(Class<I> clz, PrintStream trace);

  public <I> Class<? extends I> getMockClass(Class<I> clz);

  public boolean isMock(Class<?> clz);
    
  public boolean isMock(Object obj);

  public <T> MoxyStubber<T> when(Supplier<T> invocation);

  /**
   * Special case to allow void methods to be stubbed (without thenReturn).
   * 
   * @param invocation
   * @return A special-case MoxyVoidStubber.
   */
  public MoxyVoidStubber when(Runnable invocation);
  
  public MoxyVerifier assertMock(Runnable invocation);
}