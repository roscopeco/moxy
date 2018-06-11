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
package com.roscopeco.moxy;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.roscopeco.moxy.api.InvocationRunnable;
import com.roscopeco.moxy.api.InvocationSupplier;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.api.MoxyMultiVerifier;
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
  private static final String PRIMITIVE_BYTE_TYPE = "byte";
  private static final String PRIMITIVE_CHAR_TYPE = "char";
  private static final String PRIMITIVE_SHORT_TYPE = "short";
  private static final String PRIMITIVE_INT_TYPE = "int";
  private static final String PRIMITIVE_LONG_TYPE = "long";
  private static final String PRIMITIVE_FLOAT_TYPE = "float";
  private static final String PRIMITIVE_DOUBLE_TYPE = "double";
  private static final String PRIMITIVE_BOOLEAN_TYPE = "boolean";

  private static final Class<?>[] MOXY_ENGINE_SINGLETON_ARRAY =
      new Class<?>[] { MoxyEngine.class };

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
  public static void setMoxyEngine(final MoxyEngine moxyEngine) {
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
   * Moxy framework.</p>
   *
   * <p><strong>Note:</strong> This method will <strong>not call a constructor</strong>
   * on the generated instance, which may cause problems if you plan
   * to use #{@link MoxyStubber#thenCallRealMethod()} with this mock.</p>
   *
   * <p>If that is the case, see
   * {@link #constructSpy(Class, Object...)}.</p>
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
  public static <T> T mock(final Class<T> clz) {
    return mock(clz, (PrintStream)null);
  }

  /**
   * <p>Create a mock instance of the given class using the
   * currently-set (or default) {@link MoxyEngine}.</p>
   *
   * <p>The mock will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   *
   * <p><strong>Note:</strong> This method will <strong>not call a constructor</strong>
   * on the generated instance, which may cause problems if you plan
   * to use #{@link MoxyStubber#thenCallRealMethod()} with this mock.</p>
   *
   * <p>If that is the case, see
   * {@link #constructSpy(Class, PrintStream, Object...)}.</p>
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
  public static <T> T mock(final Class<T> clz, final PrintStream trace) {
    return mock(ensureMoxyEngine(), clz, trace);
  }

  /**
   * <p>Create a mock instance of the given class using the
   * specified {@link MoxyEngine}.</p>
   *
   * <p>The mock will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code>.</p>
   *
   * <p><strong>Note:</strong> This method will <strong>not call a constructor</strong>
   * on the generated instance, which may cause problems if you plan
   * to use #{@link MoxyStubber#thenCallRealMethod()} with this mock.</p>
   *
   * <p>If that is the case, see
   * {@link #constructSpy(MoxyEngine, Class, PrintStream, Object...)}.</p>
   *
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param clz The <code>Class</code> to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   *
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #mock(Class, PrintStream)
   * @since 1.0
   */
  public static <T> T mock(final MoxyEngine engine, final Class<T> clz, final PrintStream trace) {
    if (clz == null) {
      throw new IllegalArgumentException("Cannot mock null");
    }

    if (engine == null) {
      throw new IllegalArgumentException("Cannot mock with null engine");
    }

    return engine.mock(clz, trace);
  }

  /**
   * <p>Create a mock instance of the given class using the
   * default {@link MoxyEngine} using a constructor call.
   * This method is intended for use when one or more methods
   * will be spied (with {@link MoxyStubber#thenCallRealMethod()}.</p>
   *
   * <p>The mock will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>This method uses a best-fit algorithm to determine
   * which constructor to call based on the supplied arguments.
   * Primitive types will be boxed/unboxed automatically.</p>
   *
   * @param clz The <code>Class</code> to spy.
   * @param args Constructor arguments for the mock.
   *
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #constructMock(Class, PrintStream, Object...)
   * @see #constructMock(MoxyEngine, Class, PrintStream, Object...)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  @SafeVarargs
  public static <T> T constructMock(final Class<T> clz, final Object... args) {
    return constructMock(clz, null, args);
  }

  /**
   * <p>Create a mock instance of the given class using the
   * default {@link MoxyEngine} using a constructor call.
   * This method is intended for use when one or more methods
   * will be spied (with {@link MoxyStubber#thenCallRealMethod()}.</p>
   *
   * <p>The mock will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code> (if non-<code>null</code>).</p>
   *
   * <p>This method uses a best-fit algorithm to determine
   * which constructor to call based on the supplied arguments.
   * Primitive types will be boxed/unboxed automatically.</p>
   *
   * @param clz The <code>Class</code> to spy.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * @param args Constructor arguments for the mock.
   *
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #constructMock(Class, Object...)
   * @see #constructMock(MoxyEngine, Class, PrintStream, Object...)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  @SafeVarargs
  public static <T> T constructMock(final Class<T> clz,
                                    final PrintStream trace,
                                    final Object... args) {
    return constructMock(ensureMoxyEngine(), clz, trace, args);
  }

  /**
   * <p>Create a mock instance of the given class using the
   * supplied {@link MoxyEngine} using a constructor call.
   * This method is intended for use when one or more methods
   * will be spied (with {@link MoxyStubber#thenCallRealMethod()}.</p>
   *
   * <p>The mock will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code> (if non-<code>null</code>).</p>
   *
   * <p>This method uses a best-fit algorithm to determine
   * which constructor to call based on the supplied arguments.
   * Primitive types will be boxed/unboxed automatically.</p>
   *
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param clz The <code>Class</code> to mock.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * @param args Constructor arguments for the mock.
   *
   * @return A new mock instance.
   *
   * @param <T> The type being mocked.
   * @see #mock(Class)
   * @see #constructMock(Class, Object...)
   * @see #constructMock(Class, PrintStream, Object...)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  @SafeVarargs
  public static <T> T constructMock(final MoxyEngine engine,
                                    final Class<T> clz,
                                    final PrintStream trace,
                                    final Object... args) {
    final Class<? extends T> mockClz = engine.getMockClass(clz, trace);

    final Class<?>[] argTypes = ArrayUtils.addAll(MOXY_ENGINE_SINGLETON_ARRAY,
        (Class<?>[])Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new));

    final Constructor<? extends T> ctor =
        ConstructorUtils.getMatchingAccessibleConstructor(mockClz, argTypes);

    if (ctor == null) {
      final String typesStr =
          "[" + Arrays.stream(argTypes)
              .map(Class::getCanonicalName)
              .collect(Collectors.joining(", "))
          + "]";

      throw new IllegalArgumentException("No constructor matching argument types " + typesStr + " found");
    } else {
      try {
        return ctor.newInstance(ArrayUtils.addAll(new Object[] { engine }, args));
      } catch (final InstantiationException e) {
        throw new MoxyException("Cannot instantiate mock - instantiation exception (see cause)", e);
      } catch (final InvocationTargetException e) {
        throw new MoxyException("Cannot instantiate mock - target exception (see cause)", e);
      } catch (final IllegalAccessException e) {
        throw new MoxyException("[BUG] Cannot instantiate mock - illegal access (see cause)", e);
      }
    }
  }

  /**
   * <p>Convert the given mock instance into a spy.</p>
   *
   * <p>Note that this will reset all prior stubbing on the given mock.</p>
   *
   * <p>If the supplied object is not a mock, <code>IllegalArgumentException</code>
   * will be thrown.</p>
   *
   * @param mock The mock to convert.
   *
   * @return The mock, converted to a spy.
   * @since 1.0
   *
   * @param <T> The type of the mock.
   *
   * @see #spy(Class)
   * @see #spy(Class, PrintStream)
   * @see #spy(MoxyEngine, Class, PrintStream)
   */
  public static <T> T spy(final T mock) {
    return spy(mock, true);
  }

  /*
   * Convert mock to spy, optionally without resetting.
   *
   * Used when converting a newly-created mock.
   */
  static <T> T spy(final T mock, final boolean doReset) {
    if (!isMock(mock)) {
      throw new IllegalArgumentException("Cannot convert "
                                         + mock.toString()
                                         + " to spy - it is not a mock");
    }

    if (doReset) {
      resetMock(mock);
    }

    Arrays.stream(mock.getClass().getDeclaredMethods()).forEach(method -> {
      if (!method.getName().startsWith("__moxy_asm")) {
        Moxy.when(() -> method.invoke(mock,
              Arrays.stream(method.getParameterTypes()).map(type -> {
                switch (type.toString()) {
                case PRIMITIVE_BYTE_TYPE:
                  return Matchers.anyByte();
                case PRIMITIVE_CHAR_TYPE:
                  return Matchers.anyChar();
                case PRIMITIVE_SHORT_TYPE:
                  return Matchers.anyShort();
                case PRIMITIVE_INT_TYPE:
                  return Matchers.anyInt();
                case PRIMITIVE_LONG_TYPE:
                  return Matchers.anyLong();
                case PRIMITIVE_FLOAT_TYPE:
                  return Matchers.anyFloat();
                case PRIMITIVE_DOUBLE_TYPE:
                  return Matchers.anyDouble();
                case PRIMITIVE_BOOLEAN_TYPE:
                  return Matchers.anyBool();
                default:
                  return Matchers.any();
                }
              }).toArray())
        ).thenCallRealMethod();
      }
    });

    return mock;
  }

  /**
   * <p>Create a spy instance of the given class using the
   * default {@link MoxyEngine}.</p>
   *
   * <p>The spy will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p><strong>Note:</strong> This method will <strong>not call a constructor</strong>
   * on the generated instance, which may be more troublesome for spies
   * than it is for mocks (as the real methods being called may rely
   * on some state initialized by the constructor).</p>
   *
   * <p>If you require a constructor be called, see
   * {@link #constructSpy(Class, Object...)}.</p>
   *
   * @param clz The <code>Class</code> to spy.
   *
   * @return A new spy instance.
   *
   * @param <T> The type being spied.
   * @see #spy(Class, PrintStream)
   * @see #spy(MoxyEngine, Class, PrintStream)
   * @see #spy(Object)
   * @see #constructSpy(Class, Object...)
   * @see #mock(Class)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  public static <T> T spy(final Class<T> clz) {
    return spy(clz, null);
  }

  /**
   * <p>Create a spy instance of the given class using the
   * default {@link MoxyEngine}.</p>
   *
   * <p>The spy will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the spy, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code> (if non-<code>null</code>).</p>
   *
   * <p><strong>Note:</strong> This method will <strong>not call a constructor</strong>
   * on the generated instance, which may be more troublesome for spies
   * than it is for mocks (as the real methods being called may rely
   * on some state initialized by the constructor).</p>
   *
   * <p>If you require a constructor be called, see
   * {@link #constructSpy(Class, PrintStream, Object...)}.</p>
   *
   * @param clz The <code>Class</code> to spy.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   *
   * @return A new spy instance.
   *
   * @param <T> The type being spied.
   * @see #spy(Class)
   * @see #spy(MoxyEngine, Class, PrintStream)
   * @see #spy(Object)
   * @see #constructSpy(Class, PrintStream, Object...)
   * @see #mock(Class, PrintStream)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  public static <T> T spy(final Class<T> clz, final PrintStream trace) {
    return spy(ensureMoxyEngine(), clz, trace);
  }

  /**
   * <p>Create a spy instance of the given class using the
   * specified {@link MoxyEngine}.</p>
   *
   * <p>The spy will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the mock, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code> (if non-<code>null</code>).</p>
   *
   * <p><strong>Note:</strong> This method will <strong>not call a constructor</strong>
   * on the generated instance, which may be more troublesome for spies
   * than it is for mocks (as the real methods being called may rely
   * on some state initialized by the constructor).</p>
   *
   * <p>If you require a constructor be called, see
   * {@link #constructSpy(MoxyEngine, Class, PrintStream, Object...)}.</p>
   *
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param clz The <code>Class</code> to spy.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   *
   * @return A new spy instance.
   *
   * @param <T> The type being spied.
   * @see #spy(Class)
   * @see #spy(Class, PrintStream)
   * @see #spy(Object)
   * @see #constructSpy(MoxyEngine, Class, PrintStream, Object...)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  public static <T> T spy(final MoxyEngine engine, final Class<T> clz, final PrintStream trace) {
    return spy(engine.mock(clz, trace));
  }

  /**
   * <p>Create a spy instance of the given class using the
   * default {@link MoxyEngine} using a constructor call.</p>
   *
   * <p>The spy will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>This method uses a best-fit algorithm to determine
   * which constructor to call based on the supplied arguments.
   * Primitive types will be boxed/unboxed automatically.</p>
   *
   * @param clz The <code>Class</code> to spy.
   * @param args Constructor arguments for the spy.
   *
   * @return A new spy instance.
   *
   * @param <T> The type being spied.
   * @see #spy(Class)
   * @see #spy(Object)
   * @see #constructSpy(Class, PrintStream, Object...)
   * @see #constructSpy(MoxyEngine, Class, PrintStream, Object...)
   * @see #mock(Class)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  @SafeVarargs
  public static <T> T constructSpy(final Class<T> clz, final Object... args) {
    return constructSpy(clz, null, args);
  }

  /**
   * <p>Create a spy instance of the given class using the
   * default {@link MoxyEngine} using a constructor call.</p>
   *
   * <p>The spy will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the spy, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code> (if non-<code>null</code>).</p>
   *
   * <p>This method uses a best-fit algorithm to determine
   * which constructor to call based on the supplied arguments.
   * Primitive types will be boxed/unboxed automatically.</p>
   *
   * @param clz The <code>Class</code> to spy.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * @param args Constructor arguments for the spy.
   *
   * @return A new spy instance.
   *
   * @param <T> The type being spied.
   * @see #spy(Class, PrintStream)
   * @see #spy(Object)
   * @see #constructSpy(Class, Object...)
   * @see #constructSpy(MoxyEngine, Class, PrintStream, Object...)
   * @see #mock(Class, PrintStream)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  @SafeVarargs
  public static <T> T constructSpy(final Class<T> clz,
                                   final PrintStream trace,
                                   final Object... args) {
    return constructSpy(ensureMoxyEngine(), clz, trace, args);
  }

  /**
   * <p>Create a spy instance of the given class using the
   * supplied {@link MoxyEngine} using a constructor call.</p>
   *
   * <p>The spy will be created in the engine's default
   * <code>ClassLoader</code>. For the default engine, this
   * is the same <code>ClassLoader</code> that loaded the
   * Moxy framework.</p>
   *
   * <p>During creation of the spy, this method will also dump the
   * generated bytecode, in a debugging format, to the supplied
   * <code>PrintStream</code> (if non-<code>null</code>).</p>
   *
   * <p>This method uses a best-fit algorithm to determine
   * which constructor to call based on the supplied arguments.
   * Primitive types will be boxed/unboxed automatically.</p>
   *
   * @param engine The {@link MoxyEngine} implementation to use.
   * @param clz The <code>Class</code> to spy.
   * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
   * @param args Constructor arguments for the spy.
   *
   * @return A new spy instance.
   *
   * @param <T> The type being spied.
   * @see #spy(MoxyEngine, Class, PrintStream)
   * @see #spy(Object)
   * @see #constructSpy(Class, Object...)
   * @see #constructSpy(Class, PrintStream, Object...)
   * @see #mock(MoxyEngine, Class, PrintStream)
   * @see MoxyStubber#thenCallRealMethod()
   * @since 1.0
   */
  @SafeVarargs
  public static <T> T constructSpy(final MoxyEngine engine,
                                   final Class<T> clz,
                                   final PrintStream trace,
                                   final Object... args) {
    final T mock = constructMock(engine, clz, trace, args);
    return spy(mock, false);
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
  public static boolean isMock(final Class<?> clz) {
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
  public static boolean isMock(final Object obj) {
    return isMock(ensureMoxyEngine(), obj);
  }

  /**
   * <p>Reset the supplied mock, removing all stubbing that was previously applied.</p>
   *
   * <p>Note that this <strong>does not</strong> operate on a thread-local basis,
   * (since stubbing is not thread-local) and that it does not reset previous
   * invocation data for the mock. If you wish to reset that, see {@link MoxyEngine#reset()}.</p>
   *
   * @param mock The mock to reset.
   *
   * @see MoxyEngine#reset()
   * @see Moxy#resetMock(MoxyEngine, Object)
   * @see MoxyEngine#resetMock(Object)
   * @since 1.0
   */
  public static void resetMock(final Object mock) {
    resetMock(ensureMoxyEngine(), mock);
  }

  /**
   * <p>Reset the supplied mock using the supplied engine, removing all stubbing
   * that was previously applied.</p>
   *
   * <p>Note that this <strong>does not</strong> operate on a thread-local basis,
   * (since stubbing is not thread-local) and that it does not reset previous
   * invocation data for the mock. If you wish to reset that, see {@link MoxyEngine#reset()}.</p>
   *
   * @param engine The {@link MoxyEngine} to use.
   * @param mock The mock to reset.
   *
   * @see MoxyEngine#reset()
   * @see MoxyEngine#resetMock(Object)
   * @since 1.0
   */
  public static void resetMock(final MoxyEngine engine, final Object mock) {
    engine.resetMock(mock);
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
  public static boolean isMock(final MoxyEngine engine, final Class<?> clz) {
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
  public static boolean isMock(final MoxyEngine engine, final Object obj) {
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
   * @see #when(InvocationRunnable)
   * @see #when(MoxyEngine, InvocationSupplier)
   * @see MoxyStubber
   * @since 1.0
   */
  public static <T> MoxyStubber<T> when(final InvocationSupplier<T> invocation) {
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
   * @see #when(InvocationSupplier)
   * @see #when(MoxyEngine, InvocationRunnable)
   * @see MoxyStubber
   * @since 1.0
   */
  public static MoxyVoidStubber when(final InvocationRunnable invocation) {
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
   * @see #when(MoxyEngine, InvocationRunnable)
   * @see #when(InvocationSupplier)
   * @see MoxyStubber
   * @since 1.0
   */
  public static <T> MoxyStubber<T> when(final MoxyEngine engine, final InvocationSupplier<T> invocation) {
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
   * @see #when(InvocationSupplier)
   * @see #when(MoxyEngine, InvocationRunnable)
   * @see MoxyStubber
   * @since 1.0
   */
  public static MoxyVoidStubber when(final MoxyEngine engine, final InvocationRunnable invocation) {
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
   * @see #assertMock(MoxyEngine, InvocationRunnable)
   * @see MoxyVerifier
   * @since 1.0
   */
  public static MoxyVerifier assertMock(final InvocationRunnable invocation) {
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
   * @see #assertMock(InvocationRunnable)
   * @see MoxyVerifier
   * @since 1.0
   */
  public static MoxyVerifier assertMock(final MoxyEngine engine, final InvocationRunnable invocation) {
    return engine.assertMock(invocation);
  }

  public static MoxyMultiVerifier assertMocks(final InvocationRunnable invocation) {
    return assertMocks(ensureMoxyEngine(), invocation);
  }

  public static MoxyMultiVerifier assertMocks(final MoxyEngine engine,
                                              final InvocationRunnable invocation) {
    return engine.assertMocks(invocation);
  }
}
