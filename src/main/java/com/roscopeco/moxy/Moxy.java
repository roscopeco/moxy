package com.roscopeco.moxy;

import java.io.PrintStream;
import java.util.function.Supplier;

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
  
  public static <T> MoxyStubber<T> when(Supplier<T> invocation) {
    return when(ensureMoxyEngine(), invocation);
  }

  /**
   * Special case when to allow the same syntax to work for void
   * methods as for non-void ones.
   *  
   * @param invocation
   * @return The special-case MoxyVoidStubber. 
   */
  public static MoxyVoidStubber when(Runnable invocation) {
    return when(ensureMoxyEngine(), invocation);
  }

  public static <T> MoxyStubber<T> when(MoxyEngine engine, Supplier<T> invocation) {
    return engine.when(invocation);
  }
  
  public static MoxyVoidStubber when(MoxyEngine engine, Runnable invocation) {
    return engine.when(invocation);
  }
  
  public static MoxyVerifier assertMock(Runnable invocation) {
    return assertMock(ensureMoxyEngine(), invocation);
  }
  
  public static MoxyVerifier assertMock(MoxyEngine engine, Runnable invocation) {
    return engine.assertMock(invocation);
  }  
}
