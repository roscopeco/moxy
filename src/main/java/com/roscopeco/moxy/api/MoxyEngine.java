package com.roscopeco.moxy.api;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

import com.roscopeco.moxy.matchers.Matchers;

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
  
  /**
   * <p>Obtain the {@link MoxyMatcherEngine} that this <code>MoxyEngine</code>
   * uses to service argument matchers.</p>
   * 
   * <p>Matcher engines are closely tied to the <code>MoxyEngine</code> implementation
   * and as such there is no way to replace the matcher engine.</p>
   * 
   * @return The {@link MoxyMatcherEngine} in use by this <code>MoxyEngine</code>.
   * 
   * @since 1.0
   */
  public MoxyMatcherEngine getMatcherEngine();
  
  /**
   * <p>Reset this engine.</p>
   * 
   * <p>Implementations may choose to globally reset the mocking framework,
   * or to reset only for  the current thread. This must be documented 
   * with the engine implementation.</p>
   * 
   * <p>The default engine <code>reset()</code>s on a per-thread basis.</p>
   * 
   * @since 1.0
   */
  public void reset();
  
  /**
   * <p>Create a mock class of the given class and then define it in the
   * given <code>ClassLoader</code>.</p>
   * 
   * <p>This method allows only a specific subset of methods to be mocked.
   * Please note, however, that any abstract methods will always be mocked
   * in order to generate loadable classes. This also applies to interfaces,
   * in which all methods (except those with a <code>default</code>
   * implementation) are considered abstract.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   * 
   * @param loader The <code>ClassLoader</code> to define the new class in.
   * @param clz The original class.
   * @param methods <code>Set</code> of <code>Method</code>s to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * 
   * @return The new <code>Class</code> object.
   * 
   * @param <I> The type being mocked.
   * @since 1.0
   */
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, Set<Method> methods, PrintStream trace);

  /**
   * <p>Create a mock class of the given class and then define it in the
   * given <code>ClassLoader</code>.</p>
   * 
   * <p>This method allows only a specific subset of methods to be mocked.
   * Please note, however, that any abstract methods will always be mocked
   * in order to generate loadable classes. This also applies to interfaces,
   * in which all methods (except those with a <code>default</code>
   * implementation) are considered abstract.</p>
   *
   * @param loader The <code>ClassLoader</code> to define the new class in.
   * @param clz The original class.
   * @param methods <code>Set</code> of <code>Method</code>s to mock.
   * 
   * @return The new <code>Class</code> object.
   * 
   * @param <I> The type being mocked.
   * @see #getMockClass(Class, Set)
   * @since 1.0
   */
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, Set<Method> methods);

  /**
   * <p>Create a mock class of the given class and then define it in the
   * engine's default <code>ClassLoader</code>.</p>
   * 
   * <p>For the default engine, the default <code>ClassLoader</code> is the same
   * <code>ClassLoader</code> that loaded the class being mocked.</p>
   * 
   * <p>This method allows only a specific subset of methods to be mocked.
   * Please note, however, that any abstract methods will always be mocked
   * in order to generate loadable classes. This also applies to interfaces,
   * in which all methods (except those with a <code>default</code>
   * implementation) are considered abstract.</p>
   *
   * @param clz The original class.
   * @param methods <code>Set</code> of <code>Method</code>s to mock.
   * 
   * @return The new <code>Class</code> object.
   * 
   * @param <I> The type being mocked.
   * @see #getMockClass(ClassLoader, Class, Set)
   * @since 1.0
   */
  public <I> Class<? extends I> getMockClass(Class<I> clz, Set<Method> methods);

  /**
   * <p>Create a mock class of the given class and then define it in the
   * given <code>ClassLoader</code>.</p>
   * 
   * @param loader The <code>ClassLoader</code> to define the new class in.
   * @param clz The original class.
   * 
   * @return The new <code>Class</code> object.
   * 
   * @param <I> The type being mocked.
   * @see #getMockClass(Class, Set)
   * @since 1.0
   */
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz);

  /**
   * <p>Create a mock class of the given class and then define it in the
   * engine's default <code>ClassLoader</code>.</p>
   * 
   * <p>For the default engine, the default <code>ClassLoader</code> is the same
   * <code>ClassLoader</code> that loaded the class being mocked.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   * 
   * @param clz The original class.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * 
   * @return The new <code>Class</code> object.
   * 
   * @param <I> The type being mocked.
   * @see #getMockClass(ClassLoader, Class, Set, PrintStream)
   * @since 1.0
   */
  public <I> Class<? extends I> getMockClass(Class<I> clz, PrintStream trace);

  /**
   * <p>Create a mock class of the given class and then define it in the
   * engine's default <code>ClassLoader</code>.</p>
   * 
   * <p>For the default engine, the default <code>ClassLoader</code> is the same
   * <code>ClassLoader</code> that loaded the class being mocked.</p>
   * 
   * @param clz The original class.
   * 
   * @return The new <code>Class</code> object.
   * 
   * @param <I> The type being mocked.
   * @see #getMockClass(ClassLoader, Class)
   * @since 1.0
   */
  public <I> Class<? extends I> getMockClass(Class<I> clz);

  /**
   * <p>Create a mock instance of the given class.</p>
   * 
   * <p>The mock will be created in the engine's default <code>ClassLoader</code>. 
   * For the default engine, this is the same <code>ClassLoader</code> that
   * loaded the engine.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   *  
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class, PrintStream)
   * @since 1.0
   */
  public <T> T mock(Class<T> clz);

  /**
   * <p>Create a mock instance of the given class.</p>
   * 
   * <p>The mock will be created in the engine's default <code>ClassLoader</code>. 
   * For the default engine, this is the same <code>ClassLoader</code> that
   * loaded the engine.</p>
   * 
   * <p>During creation of the mock, this method will also dump the generated
   * bytecode, in a debugging format, to the supplied <code>PrintStream</code>.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   *  
   * @return A new mock instance.
   * 
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @since 1.0
   */
  public <T> T mock(Class<T> clz, PrintStream trace);
  
  /**
   * <p>Determines whether the supplied class is a mock class.</p>
   * 
   * <p>How this determination is made is implementation-dependent, but may
   * rely on the fact that the class has the (mandatory) 
   * {@link com.roscopeco.moxy.api.Mock} annotation.</p>
   * 
   * @param clz The class to query.
   * 
   * @return <code>true</code> if the class is a mock, <code>false</code> otherwise.
   * 
   * @see #isMock(Object)
   * @since 1.0
   */
  public boolean isMock(Class<?> clz);
    
  /**
   * <p>Determines whether the supplied object is a mock instance.</p>
   * 
   * <p>How this determination is made is implementation-dependent, but may
   * rely on the fact that the class has the (mandatory) 
   * {@link com.roscopeco.moxy.api.Mock} annotation.</p>
   * 
   * @param obj The object to query.
   * 
   * @return <code>true</code> if the class is a mock, <code>false</code> otherwise.
   * 
   * @see #isMock(Class)
   * @since 1.0
   */
  public boolean isMock(Object obj);

  /**
   * <p>Starts stubbing of the mock invocation in the supplied lambda 
   * expression.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be stubbed and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * engine.when(() -&gt; mock.someMethod("one", "two")).thenReturn("three");
   * </code></pre>
   * 
   * <p>The arguments passed to the mock within the lambda may be either
   * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
   * 
   * <p>See {@link MoxyStubber} for details on the stubbing methods
   * available.</p>
   * 
   * @param invocation A lambda that will invoke the method to be stubbed.
   * 
   * @return A {@link MoxyStubber} that will stub the given method.
   * 
   * @param <T> The type being stubbed (the return type of the mocked method).
   * @see #when(Runnable)
   * @see MoxyStubber
   * @since 1.0
   */
  public <T> MoxyStubber<T> when(Supplier<T> invocation);

  /**
   * <p>Starts stubbing of the mock invocation in the supplied lambda 
   * expression.</p>
   * 
   * <p>The Java compiler will automatically select this overload of
   * the <code>when</code> method when the method to be stubbed is
   * void.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be stubbed and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * engine.when(() -&gt; mock.voidMethod("one", "two")).thenThrow("three");
   * </code></pre>
   * 
   * <p>The arguments passed to the mock within the lambda may be either
   * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
   * 
   * <p>See {@link MoxyStubber} for details on the stubbing methods
   * available.</p>
   *  
   * @param invocation A lambda that will invoke the method to be stubbed.
   * 
   * @return A {@link MoxyVoidStubber} that will stub the given method.
   * 
   * @see #when(Supplier)
   * @see MoxyStubber
   * @since 1.0
   */
  public MoxyVoidStubber when(Runnable invocation);
  
  /**
   * <p>Starts verification of the mock invocation in the supplied lambda 
   * expression.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be verified and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * engine.assertMock(() -&gt; mock.voidMethod(engine, "one", "two")).wasCalled();
   * </code></pre>
   * 
   * <p>The arguments passed to the mock within the lambda may be either
   * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
   * 
   * <p>See {@link MoxyStubber} for details on the stubbing methods
   * available.</p>
   * 
   * @param invocation A lambda that will invoke the method to be stubbed.
   * 
   * @return A {@link MoxyVerifier} that will verify the given method.
   *
   * @see MoxyVerifier
   * @since 1.0
   */
  public MoxyVerifier assertMock(Runnable invocation);
}