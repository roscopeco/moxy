package com.roscopeco.moxy;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.api.MoxyVoidStubber;
import com.roscopeco.moxy.impl.asm.ASMMoxyEngine;
import com.roscopeco.moxy.matchers.Matchers;

/**
 * <p>Moxy is a type-safe mocking/spying framework for Java with a fluent API.</p>
 * 
 * <p>It takes inspiration from a number of other popular mocking frameworks,
 * notably Mockito. It aims to be lean, fast, and as far as possible to lack
 * the "surprises" one finds in other mocking frameworks (e.g. it should not
 * give weird failures in unrelated tests, but instead should fail fast when
 * used improperly).</p>
 * 
 * <p>This class, along with the {@link Matchers} class, are the main top-level
 * classes most users will need to interact with when using Moxy. They are
 * designed so you can simply <code>import static</code> and start mocking. 
 * For example:</p>
 * 
 * <pre><code>
 * import static com.roscopeco.moxy.Moxy.*;
 * import static com.roscopeco.moxy.matchers.Matchers.*;
 * 
 * // ... later ...
 * 
 * SomeClass mock = mock(SomeClass.class);
 * 
 * when(() -&gt; mock.someMethod(any(), eq("something"))).thenReturn("whatever");
 * 
 * // ... mock usage ...
 * 
 * assertMock(() -&gt; mock.someMethod("expected", "something").wasCalledTwice();
 * assertMock(() -&gt; mock.someMethod("badfood", "something").wasNotCalled();
 * </code></pre>
 * 
 * See <code>README.md</code> for more detailed usage of the library.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 * @see com.roscopeco.moxy.matchers.Matchers
 */
public final class Moxy {
  private static final Logger LOG = Logger.getLogger(Moxy.class.getName());
  
  private static MoxyEngine moxyEngine;
  
  private Moxy() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.Moxy is not designed for instantiation");
  }
  
  private static MoxyEngine ensureMoxyEngine() {
    if (moxyEngine == null) {
      moxyEngine = new ASMMoxyEngine();
    }
    
    return moxyEngine;
  }
  
  /**
   * <p>Set the {@link MoxyEngine} that will be used by all future calls
   * to methods in this class.</p>
   * 
   * <p><strong>Note:</strong> Calling this method after the current
   * (or default) engine has been used may cause problems. If you are
   * changing the engine, you should change it before interacting
   * with the library in any other way (especially creating mocks).</p>
   * 
   * @param moxyEngine The {@link MoxyEngine} implementation to use.
   * 
   * @since 1.0
   */
  public static void setMoxyEngine(MoxyEngine moxyEngine) {
    if (Moxy.moxyEngine != null) {
      LOG.warning(() -> "Changing an in-use Moxy engine may cause unwanted side effects");
    }
    
    Moxy.moxyEngine = moxyEngine;
  }
  
  /**
   * Get the current {@link MoxyEngine} in use by this class, or
   * create and return a default engine if none has been set.
   * 
   * @return The {@link MoxyEngine} currently in use.
   * 
   * @since 1.0
   */
  public static MoxyEngine getMoxyEngine() {
    return Moxy.ensureMoxyEngine();
  }
  
  /**
   * <p>Create a mock instance of the given class using the 
   * currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The mock will be created in the engine's default 
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the 
   * class being mocked.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   *  
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class, PrintStream)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(Class<T> clz) {
    return mock(clz, (PrintStream)null);
  }
  
  /**
   * <p>Create a mock instance of the given class using the 
   * currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The mock will be created in the engine's default 
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the 
   * class being mocked.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   * @param methods The <code>java.lang.reflect.Method</code>s to mock.
   *  
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class, PrintStream)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(Class<T> clz, Method... methods) {    
    return mock(clz, Arrays.stream(methods).collect(Collectors.toSet()));
  }
  
  /**
   * <p>Create a mock instance of the given class using the 
   * currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The mock will be created in the engine's default 
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the 
   * class being mocked.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   * @param methods The <code>java.lang.reflect.Method</code>s to mock.
   *  
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class, PrintStream)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(Class<T> clz, Set<Method> methods) {
    return mock(clz, methods, null);
  }
  
  /**
   * <p>Create a mock instance of the given class using the 
   * currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The mock will be created in the engine's default 
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the 
   * class being mocked.</p>
   * 
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   *  
   * @return A new mock instance.
   * 
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(Class<T> clz, PrintStream trace) {
    return mock(clz, MoxyEngine.ALL_METHODS, trace);
  }

  /**
   * <p>Create a mock instance of the given class using the 
   * currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The mock will be created in the engine's default 
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the 
   * class being mocked.</p>
   * 
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   * 
   * @param clz The <code>Class</code> to mock.
   * @param methods The <code>java.lang.reflect.Method</code>s to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * 
   * @return A new mock instance.
   * 
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(Class<T> clz, Set<Method> methods, PrintStream trace) {
    return mock(ensureMoxyEngine(), clz, methods, trace);
  }

  /**
   * <p>Create a mock instance of the given class using the 
   * specified {@link MoxyEngine}.</p>
   * 
   * <p>The mock will be created in the engine's default 
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the 
   * class being mocked.</p>
   * 
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   * 
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param clz The <code>Class</code> to mock.
   * @param methods The <code>java.lang.reflect.Method</code>s to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   *  
   * @return A new mock instance.
   * 
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #mock(Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(MoxyEngine engine, Class<T> clz, Set<Method> methods, PrintStream trace) {
    if (clz == null) {
      throw new IllegalArgumentException("Cannot mock null");
    }
    
    if (engine == null) {
      throw new IllegalArgumentException("Cannot mock with null engine");      
    }
    
    return engine.mock(clz, methods, trace);
  }

  /**
   * <p>Determines whether the supplied class is a mock class.</p>
   * 
   * <p>How this determination is made is engine-dependent, but may
   * rely on the fact that the class has the (mandatory) 
   * {@link com.roscopeco.moxy.api.Mock} annotation.</p>
   * 
   * @param clz The class to query.
   * 
   * @return <code>true</code> if the class is a mock, <code>false</code> otherwise.
   * 
   * @since 1.0
   */
  public static boolean isMock(Class<?> clz) {
    return isMock(ensureMoxyEngine(), clz);
  }
  
  /**
   * <p>Determines whether the supplied object is a mock instance.</p>
   * 
   * <p>How this determination is made is engine-dependent, but may
   * rely on the fact that the class has the (mandatory) 
   * {@link com.roscopeco.moxy.api.Mock} annotation.</p>
   * 
   * @param obj The object to query.
   * 
   * @return <code>true</code> if the object is a mock, <code>false</code> otherwise.
   * 
   * @since 1.0
   */
  public static boolean isMock(Object obj) {
    return isMock(ensureMoxyEngine(), obj);
  }  

  /**
   * <p>Determines whether the supplied class is a mock class in the context
   * of the supplied {@link MoxyEngine}.</p>
   * 
   * <p>How this determination is made is engine-dependent.</p>
   * 
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param clz The class to query.
   * 
   * @return <code>true</code> if the class is a mock, <code>false</code> otherwise.
   * 
   * @since 1.0
   */
  public static boolean isMock(MoxyEngine engine, Class<?> clz) {
    return engine.isMock(clz);
  }
  
  /**
   * <p>Determines whether the supplied class is a mock class in the context
   * of the supplied {@link MoxyEngine}.</p>
   * 
   * <p>How this determination is made is engine-dependent.</p>
   * 
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param obj The object to query.
   * 
   * @return <code>true</code> if the object is a mock, <code>false</code> otherwise.
   * 
   * @since 1.0
   */
  public static boolean isMock(MoxyEngine engine, Object obj) {
    return engine.isMock(obj);
  }

  /**
   * <p>Starts stubbing of the mock invocation in the supplied lambda 
   * expression using the currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be stubbed and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * Moxy.when(() -&gt; mock.someMethod("one", "two")).thenReturn("three");
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
   * @param <T> The type being stubbed (return type of the mock method).
   * @see #when(Runnable)
   * @see #when(MoxyEngine, Supplier)
   * @see MoxyStubber
   * @since 1.0
   */
  public static <T> MoxyStubber<T> when(Supplier<T> invocation) {
    return when(ensureMoxyEngine(), invocation);
  }

  /**
   * <p>Starts stubbing of the mock invocation in the supplied lambda 
   * expression using the currently-set (or default) {@link MoxyEngine}.</p>
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
   * Moxy.when(() -&gt; mock.voidMethod("one", "two")).thenThrow("three");
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
   * @see #when(Supplier)
   * @see #when(MoxyEngine, Runnable)
   * @see MoxyStubber
   * @since 1.0
   */
  public static MoxyVoidStubber when(Runnable invocation) {
    return when(ensureMoxyEngine(), invocation);
  }

  /**
   * <p>Starts stubbing of the mock invocation in the supplied lambda 
   * expression using the specified {@link MoxyEngine}.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be stubbed and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * Moxy.when(engine, () -&gt; mock.someMethod("one", "two")).thenReturn("three");
   * </code></pre>
   * 
   * <p>The arguments passed to the mock within the lambda may be either
   * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
   * 
   * <p>See {@link MoxyStubber} for details on the stubbing methods
   * available.</p>
   * 
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param invocation A lambda that will invoke the method to be stubbed.
   * 
   * @return A {@link MoxyStubber} that will stub the given method.
   * 
   * @param <T> The type being stubbed (return type of the mocked method).
   * @see #when(MoxyEngine, Runnable)
   * @see #when(Supplier)
   * @see MoxyStubber
   * @since 1.0
   */
  public static <T> MoxyStubber<T> when(MoxyEngine engine, Supplier<T> invocation) {
    return engine.when(invocation);
  }
  
  /**
   * <p>Starts stubbing of the mock invocation in the supplied lambda 
   * expression using the specified {@link MoxyEngine}.</p>
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
   * Moxy.when(() -&gt; mock.voidMethod(engine, "one", "two")).thenThrow("three");
   * </code></pre>
   * 
   * <p>The arguments passed to the mock within the lambda may be either
   * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
   * 
   * <p>See {@link MoxyStubber} for details on the stubbing methods
   * available.</p>
   *  
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param invocation A lambda that will invoke the method to be stubbed.
   * 
   * @return A {@link MoxyStubber} that will stub the given method.
   * 
   * @see #when(Supplier)
   * @see #when(MoxyEngine, Runnable)
   * @see MoxyStubber
   * @since 1.0
   */
  public static MoxyVoidStubber when(MoxyEngine engine, Runnable invocation) {
    return engine.when(invocation);
  }
  
  /**
   * <p>Starts verification of the mock invocation in the supplied lambda 
   * expression using the currently-set (or default) {@link MoxyEngine}.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be verified and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * Moxy.assertMock(() -&gt; mock.voidMethod(engine, "one", "two")).wasCalled();
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
   * @see #assertMock(MoxyEngine, Runnable)
   * @see MoxyVerifier
   * @since 1.0
   */
  public static MoxyVerifier assertMock(Runnable invocation) {
    return assertMock(ensureMoxyEngine(), invocation);
  }
  
  /**
   * <p>Starts verification of the mock invocation in the supplied lambda 
   * expression using the specified {@link MoxyEngine}.</p>
   * 
   * <p>The invocation of the mock is used to determine which method
   * and argument combination is to be verified and is not counted
   * toward mock invocation, return or throw counters.</p>
   * 
   * <p>Example usage:</p>
   * 
   * <pre><code>
   * Moxy.assertMock(() -&gt; mock.voidMethod(engine, "one", "two")).wasCalled();
   * </code></pre>
   * 
   * <p>The arguments passed to the mock within the lambda may be either
   * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
   * 
   * <p>See {@link MoxyStubber} for details on the stubbing methods
   * available.</p>
   * 
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param invocation A lambda that will invoke the method to be stubbed.
   * 
   * @return A {@link MoxyVerifier} that will verify the given method.
   *
   * @see #assertMock(Runnable)
   * @see MoxyVerifier
   * @since 1.0
   */
  public static MoxyVerifier assertMock(MoxyEngine engine, Runnable invocation) {
    return engine.assertMock(invocation);
  }  
}
