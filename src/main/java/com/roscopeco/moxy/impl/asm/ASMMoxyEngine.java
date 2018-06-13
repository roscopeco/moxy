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

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

import com.roscopeco.moxy.api.InvocationRunnable;
import com.roscopeco.moxy.api.InvocationSupplier;
import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MockGenerationException;
import com.roscopeco.moxy.api.MonitoredInvocationException;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.api.MoxyMultiVerifier;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.api.MoxyVoidStubber;
import com.roscopeco.moxy.impl.asm.visitors.AbstractMoxyTypeVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockClassVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockInterfaceVisitor;
import com.roscopeco.moxy.matchers.MoxyMatcher;
import com.roscopeco.moxy.matchers.PossibleMatcherUsageError;

/**
 * Default MoxyEngine implementation.
 *
 * Based on bytecode generation with ASM.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 *
 */
public class ASMMoxyEngine implements MoxyEngine {
  /*
   * Functional interface for monitored invocations.
   *
   * See #runMonitoredInvocation
   */
  @FunctionalInterface
  static interface InvocationMonitor {
    public void invoke() throws Exception;
  }

  private static final String UNRECOVERABLE_ERROR = "Unrecoverable Error";
  private static final String CANNOT_MOCK_NULL_CLASS = "Cannot mock null class";

  private final ThreadLocal<Boolean> threadLocalMockBehaviourDisabled;
  private final ThreadLocalInvocationRecorder recorder;
  private final ASMMoxyMatcherEngine matcherEngine;

  @SuppressWarnings("restriction")
  private static final sun.misc.Unsafe UNSAFE = getUnsafe();

  @SuppressWarnings("restriction")
  private static sun.misc.Unsafe getUnsafe() {
    try {
      final Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      unsafeField.setAccessible(true);
      return (sun.misc.Unsafe)unsafeField.get(null);
    } catch (final NoSuchFieldException e) {
      throw new IllegalStateException("Unrecoverable Error: NoSuchFieldException 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    } catch (final IllegalAccessException e) {
      throw new IllegalStateException("Unrecoverable Error: IllegalAccessException when accessing 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    }
  }

  /**
   * Construct a new instance of the ASMMoxyEngine.
   *
   * @since 1.0
   */
  public ASMMoxyEngine() {
    this.recorder = new ThreadLocalInvocationRecorder(this);
    this.matcherEngine = new ASMMoxyMatcherEngine(this);
    this.threadLocalMockBehaviourDisabled = new ThreadLocal<>();
    this.threadLocalMockBehaviourDisabled.set(false);
  }

  ASMMoxyEngine(final ThreadLocalInvocationRecorder recorder, final ASMMoxyMatcherEngine matcherEngine) {
    this.recorder = recorder;
    this.matcherEngine = matcherEngine;
    this.threadLocalMockBehaviourDisabled = new ThreadLocal<>();
    this.threadLocalMockBehaviourDisabled.set(false);
  }

  /*
   * Obtain the invocation recorder used by this engine.
   */
  ThreadLocalInvocationRecorder getRecorder() {
    return this.recorder;
  }

  ASMMoxyMatcherEngine getMatcherEngine() {
    return this.matcherEngine;
  }

  /**
   * Reset this engine. In this implementation, this discards all
   * prior invocation data for the current thread.
   */
  @Override
  public void reset() {
    this.getRecorder().reset();
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#mock(java.lang.Class)
   */
  @Override
  public <T> T mock(final Class<T> clz) {
    return this.mock(clz, null);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#mock(java.lang.Class, java.io.PrintStream)
   */
  @Override
  public <T> T mock(final Class<T> clz, final PrintStream trace) {
    if (clz == null) {
      throw new IllegalArgumentException(CANNOT_MOCK_NULL_CLASS);
    }

    try {
      final Class<? extends T> mockClass = this.getMockClass(clz, MoxyEngine.ALL_METHODS, trace);
      return this.instantiateMock(mockClass);
    } catch (final MoxyException e) {
      throw e;
    } catch (final Exception e) {
      throw new MockGenerationException("Unrecoverable error: exception during mock generation", e);
    }
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class, java.util.Set, java.io.PrintStream)
   */
  @Override
  @SuppressWarnings("unchecked")
  public <I> Class<? extends I> getMockClass(final ClassLoader loader,
                                             final Class<I> clz,
                                             final Set<Method> methods,
                                             final PrintStream trace) {
    try {
      return (Class<I>)this.defineClass(loader, this.createMockClassNode(clz, methods, trace));
    } catch (final IOException e) {
      throw new MoxyException(UNRECOVERABLE_ERROR, e);
    }
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class, java.util.Set)
   */
  @Override
  public <I> Class<? extends I> getMockClass(final ClassLoader loader,
                                             final Class<I> clz,
                                             final Set<Method> methods) {
    return this.getMockClass(loader, clz, methods, null);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class, java.util.Set)
   */
  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz,
                                             final Set<Method> methods) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, methods, null);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class)
   */
  @Override
  public <I> Class<? extends I> getMockClass(final ClassLoader loader, final Class<I> clz) {
    return this.getMockClass(loader, clz, MoxyEngine.ALL_METHODS, null);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#getMockClass(java.lang.Class, java.util.Set, java.io.PrintStream)
   */
  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz, final Set<Method> methods, final PrintStream trace) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, methods, trace);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class, java.io.PrintStream)
   */
  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz, final PrintStream trace) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, MoxyEngine.ALL_METHODS, trace);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class)
   */
  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#isMock(java.lang.Class)
   */
  @Override
  public boolean isMock(final Class<?> clz) {
    return clz.getAnnotation(Mock.class) != null;
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#isMock(java.lang.Object)
   */
  @Override
  public boolean isMock(final Object obj) {
    return this.isMock(obj.getClass());
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#resetMock(java.lang.Object)
   */
  @Override
  public void resetMock(final Object mock) {
    if (!this.isMock(mock)) {
      throw new IllegalArgumentException("Cannot reset '" + mock.toString() + "' - Object is not a mock");
    }

    this.initializeMock(mock.getClass(), mock);
  }

  /*
   * Determine whether the supplied method is a candidate for mocking.
   */
  boolean isMockCandidate(final Method m) {
    return ((m.getModifiers() & Opcodes.ACC_FINAL) == 0)
        && (((m.getModifiers() & Opcodes.ACC_PUBLIC) > 0)
            || ((m.getModifiers() & Opcodes.ACC_PROTECTED) > 0)
            || ((m.getModifiers() & Opcodes.ACC_PRIVATE) == 0));
  }

  /*
   * Gather all mock-candidate methods on the given class.
   */
  HashSet<Method> gatherAllMockableMethods(final Class<?> originalClass) {
    final HashSet<Method> methods = new HashSet<>();

    for (final Method m : originalClass.getDeclaredMethods()) {
      if (this.isMockCandidate(m)) {
        methods.add(m);
      }
    }

    return methods;
  }

  /*
   * Run a monitored invocation and returns the Invocations that were
   * recorded.
   *
   * Ensures engine and matcher stack consistency, and
   * guarantees consistency at exit, whether through return or throw.
   *
   * During execution of this method mock behaviour is disabled
   * (see #disableMockBehaviourOnThisThread), and is guaranteed to
   * be re-enabled at exit.
   */
  List<Invocation> runMonitoredInvocation(final InvocationMonitor monitor) {
    // Don't do this in the try, or if it throws, the finally will too...
    this.startMonitoredInvocation();
    try {
      monitor.invoke();
      return this.getRecorder().getCurrentMonitoredInvocations();
    } catch (final MoxyException e) {
      // Framework exception; don't wrap
      throw(e);
    } catch (final NullPointerException e) {
      // Often an autoboxing error, give (hopefully) useful error message.
      if (!e.getStackTrace()[0].getClassName().startsWith("com.roscopeco.moxy.impl")) {
        // Not in our code, so almost certainly an autobox issue
        throw new PossibleMatcherUsageError(
            "If you're using primitive matchers, ensure you're using the "
          + "correct type (e.g. anyInt() rather than any()), especially when nesting", e);
      } else {
        throw new MoxyException("[BUG] NPE in engine invocation code; Probable framework bug", e);
      }
    } catch (final Exception e) {
      // Wrap in framework exception
      throw new MonitoredInvocationException(e);
    } finally {
      this.endMonitoredInvocation();
    }
  }

  /*
   * Starts a monitored invocation.
   *
   * * Ensures stack consistency (throws if inconsistent)
   * * Disables mock behaviour on the current thread.
   * * Pushes a new frame to the monitored invocation stack.
   *
   * In the disabled state, mocks will still record their invocations,
   * but will not execute actions or answers, return stubbed values (they
   * will instead return default values), or callSuper.
   *
   * This is used at the start of #runMonitoredInvocation.
   */
  void startMonitoredInvocation() {
    this.getMatcherEngine().ensureStackConsistency();
    this.threadLocalMockBehaviourDisabled.set(true);
    this.getRecorder().startMonitoredInvocation();
  }

  /*
   * Ends a monitored invocation.
   *
   * * Pops the current frame from the monitored invocation stack and discards it.
   * * Enables mock behaviour on the current thread.
   * * Ensures no matchers remain on the stack (throws if they do).
   *
   * This is guaranteed to be called at the end of
   * #runMonitoredInvocation.
   */
  void endMonitoredInvocation() {
    if (!this.threadLocalMockBehaviourDisabled.get()) {
      throw new IllegalStateException("[BUG] Attempt to end an unstarted monitored invocation (in engine)");
    }

    this.getRecorder().endMonitoredInvocation();
    this.threadLocalMockBehaviourDisabled.set(false);

    // Do this at end to guarantee we've ended the invocation,
    // as it may throw.
    this.getMatcherEngine().ensureStackConsistency();
  }

  /*
   * Determines whether mock behaviour is disabled on the
   * current thread.
   */
  boolean isMockStubbingDisabledOnThisThread() {
    final Boolean disabled = this.threadLocalMockBehaviourDisabled.get();
    return (disabled != null && disabled);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#when(com.roscopeco.moxy.api.InvocationSupplier)
   */
  @Override
  public <T> MoxyStubber<T> when(final InvocationSupplier<T> invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::get);
    return new ASMMoxyStubber<>(this, invocations);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#when(com.roscopeco.moxy.api.InvocationRunnable)
   */
  @Override
  public MoxyVoidStubber when(final InvocationRunnable invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::run);
    return new ASMMoxyVoidStubber(this, invocations);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#assertMock(com.roscopeco.moxy.api.InvocationRunnable)
   */
  @Override
  public MoxyVerifier assertMock(final InvocationRunnable invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::run);
    return new ASMMoxyVerifier(this, invocations);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#assertMocks(com.roscopeco.moxy.api.InvocationRunnable)
   */
  @Override
  public MoxyMultiVerifier assertMocks(final InvocationRunnable invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::run);
    return new ASMMoxyMultiVerifier(this, invocations);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#registerMatcher(com.roscopeco.moxy.matchers.MoxyMatcher)
   */
  @Override
  public void registerMatcher(final MoxyMatcher<?> matcher) {
    this.getMatcherEngine().registerMatcher(matcher);
  }

  /*
   * Instantiate a mock of the given mock class without
   * calling a constructor.
   *
   * This method uses sun.misc.Unsafe.
   *
   * Throws IllegalArgumentException if the given class is not
   * a mock class.
   */
  @SuppressWarnings("restriction")
  <T> T instantiateMock(final Class<? extends T> mockClass) {
    if (!this.isMock(mockClass)) {
      throw new IllegalArgumentException("Cannot instantiate " + mockClass +": it is not a mock class");
    }

    try {
      final Object mock = UNSAFE.allocateInstance(mockClass);
      return this.initializeMock(mockClass, mock);
    } catch (final MoxyException e) {
      throw e;
    } catch (final Exception e) {
      throw new MoxyException("Unrecoverable error: Instantiation exception; see cause", e);
    }
  }

  /*
   * Initialize a mock of the given class, setting all mock-related
   * synthetic fields to default values.
   *
   * When called on an existing mock, this method has the effect of
   * resetting all stubbing on that mock.
   */
  @SuppressWarnings({ "unchecked", "restriction" })
  <T> T initializeMock(final Class<? extends T> mockClass, final Object mock) {
    try {
      final Field ivarsField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_ivars_FIELD_NAME);
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(ivarsField), new ASMMockInstanceVars(this));
      return (T)mock;
    } catch (final Exception e) {
      throw new MoxyException("Unrecoverable error: Instantiation exception; see cause", e);
    }
  }

  /*
   * Actual generator; create the ASM ClassNode for a mock.
   */
  ClassNode createMockClassNode(final Class<?> clz, final Set<Method> methods, final PrintStream trace) throws IOException {
    if (clz == null) {
      throw new IllegalArgumentException(CANNOT_MOCK_NULL_CLASS);
    }

    if ((clz.getModifiers() & Opcodes.ACC_FINAL) > 0) {
      throw new MockGenerationException("Mocking of final classes is not yet supported");
    }

    final String clzInternalName = Type.getInternalName(clz);
    final ClassReader reader = new ClassReader(clzInternalName);

    // Find the annotated methods (and their interfaces)

    Set<Method> mockableMethods;
    if (methods == MoxyEngine.ALL_METHODS) {
      mockableMethods = this.gatherAllMockableMethods(clz);
    } else {
      mockableMethods = methods;
    }

    final AbstractMoxyTypeVisitor visitor =
        clz.isInterface() ?
            new MoxyMockInterfaceVisitor(clz)               :
            new MoxyMockClassVisitor(clz, mockableMethods)  ;

    reader.accept(visitor, ClassReader.SKIP_DEBUG);

    if (trace != null) {
      visitor.getNode().accept(new TraceClassVisitor(new PrintWriter(trace)));
    }

    return visitor.getNode();
  }

  /*
   * Define a class given a loader and an ASM ClassNode.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  Class<?> defineClass(final ClassLoader loader, final ClassNode node) {
    if (loader == null) {
      throw new IllegalArgumentException("Implicit definition in the system classloader is unsupported.\n"
                                       + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
                                       + "If you're sure this is what you want to do, pass system loader explicitly (rather than null)");
    }

    final byte[] code = this.generateBytecode(node);
    return UNSAFE.defineClass(node.name.replace('/',  '.'), code, 0, code.length, loader, null);
  }

  /*
   * Transform a ClassNode into bytecode.
   */
  byte[] generateBytecode(final ClassNode node) {
    final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    node.accept(writer);
    return writer.toByteArray();
  }
}
