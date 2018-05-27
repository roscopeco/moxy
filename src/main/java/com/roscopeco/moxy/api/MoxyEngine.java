package com.roscopeco.moxy.api;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

/**
 * <p>A MoxyEngine is the class that is responsible for generating mocks
 * according to whatever strategy it is designed to use.</p>
 * 
 * <p>It usually also coordinates activities of mocks, as well as 
 * integrating the matchers (in concert with {@link MoxyMatcherEngine}).</p>
 * 
 * <p>The default implementation uses the ASM library to generate mocks
 * at runtime. If you wish to replace that implementation, see 
 * {@link com.roscopeco.moxy.Moxy#setMoxyEngine(MoxyEngine)}.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
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
   * 
   * @param <I> The type of the class being mocked.
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
   * @param invocation A runnable that invokes the mock method.
   * 
   * @return A special-case MoxyVoidStubber.
   */
  public MoxyVoidStubber when(Runnable invocation);
  
  public MoxyVerifier assertMock(Runnable invocation);
}