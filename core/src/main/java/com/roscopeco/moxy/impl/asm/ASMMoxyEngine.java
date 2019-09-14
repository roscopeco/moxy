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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.roscopeco.moxy.api.ClassDefinitionStrategy;
import com.roscopeco.moxy.api.DefaultReturnGenerator;
import com.roscopeco.moxy.api.InvocationRunnable;
import com.roscopeco.moxy.api.InvocationSupplier;
import com.roscopeco.moxy.api.MockGenerationException;
import com.roscopeco.moxy.api.MonitoredInvocationException;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.api.MoxyMock;
import com.roscopeco.moxy.api.MoxyMultiVerifier;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.api.MoxyVoidStubber;
import com.roscopeco.moxy.impl.asm.visitors.AbstractMoxyTypeVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockClassVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockInterfaceVisitor;
import com.roscopeco.moxy.api.MoxyMatcher;
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
  interface InvocationMonitor {
    @SuppressWarnings("squid:S00112" /* This is a wrapper for client code */)
    void invoke() throws Exception;
  }

  private static final String UNRECOVERABLE_ERROR = "Unrecoverable Error";
  private static final String CANNOT_MOCK_NULL_CLASS = "Cannot mock null class";

  @SuppressWarnings("squid:S5164" /* Tests hopefully aren't using pools... */)
  private final ThreadLocal<Boolean> threadLocalMockBehaviourDisabled;
  private final InvocationRecorder recorder;
  private final ASMMoxyMatcherEngine matcherEngine;
  private final Map<String, DefaultReturnGenerator> returnGeneratorMap;

  /**
   * Construct a new instance of the ASMMoxyEngine.
   *
   * @since 1.0
   */
  public ASMMoxyEngine() {
    this.recorder = new InvocationRecorder(this);
    this.matcherEngine = new ASMMoxyMatcherEngine(this);
    this.threadLocalMockBehaviourDisabled = new ThreadLocal<>();
    this.threadLocalMockBehaviourDisabled.set(false);
    this.returnGeneratorMap = new HashMap<>();

    this.registerDefaultReturnGenerators();
  }

  ASMMoxyEngine(final InvocationRecorder recorder, final ASMMoxyMatcherEngine matcherEngine) {
    this.recorder = recorder;
    this.matcherEngine = matcherEngine;
    this.threadLocalMockBehaviourDisabled = new ThreadLocal<>();
    this.threadLocalMockBehaviourDisabled.set(false);
    this.returnGeneratorMap = new HashMap<>();

    this.registerDefaultReturnGenerators();
  }

  /*
   * Obtain the invocation recorder used by this engine.
   */
  InvocationRecorder getRecorder() {
    return this.recorder;
  }

  ASMMoxyMatcherEngine getMatcherEngine() {
    return this.matcherEngine;
  }

  Object getDefaultReturn(final String className) {
    final DefaultReturnGenerator gen = this.returnGeneratorMap.get(className);

    if (gen != null) {
      return this.returnGeneratorMap.get(className).generateDefaultReturnValue();
    } else {
      return null;
    }
  }

  @Override
  public void registerDefaultReturnForType(final String type, final DefaultReturnGenerator generator) {
    this.returnGeneratorMap.put(type, generator);
  }

  @Override
  public void removeDefaultReturnForType(final String type) {
    this.returnGeneratorMap.remove(type);
  }

  @Override
  public void resetDefaultReturnTypes() {
    this.returnGeneratorMap.clear();
    this.registerDefaultReturnGenerators();
  }

  private void registerDefaultReturnGenerators() {
    this.registerDefaultReturnForType(Optional.class.getName(), Optional::empty);
  }

  /**
   * Reset this engine. Discards all prior invocation data.
   */
  @Override
  public void reset() {
    this.getRecorder().reset();
  }

  @Override
  public <T> T mock(final Class<T> clz) {
    return this.mock(clz, (PrintStream)null);
  }

  @Override
  public <T> T mock(final Class<T> clz, ClassDefinitionStrategy definitionStrategy, final PrintStream trace) {
    if (clz == null) {
      throw new IllegalArgumentException(CANNOT_MOCK_NULL_CLASS);
    }

    try {
      final Class<? extends T> mockClass = this.getMockClass(clz, definitionStrategy, MoxyEngine.ALL_METHODS, trace);
      return this.instantiateMock(mockClass);
    } catch (final MoxyException e) {
      throw e;
    } catch (final Exception e) {
      throw new MockGenerationException("Unrecoverable error: exception during mock generation", e);
    }
  }

  @Override
  public <T> T mock(Class<T> clz, ClassDefinitionStrategy definitionStrategy) {
    return this.mock(clz, definitionStrategy, null);
  }

  @Override
  public <T> T mock(Class<T> clz, PrintStream trace) {
    return this.mock(clz, getDefaultClassDefinitionStrategy(), null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods, PrintStream trace) {
    try {
      return definitionStrategy.defineClass(
          loader, clz, generateBytecode(this.createMockClassNode(clz, methods, trace)));
    } catch (final IOException e) {
      throw new MoxyException(UNRECOVERABLE_ERROR, e);
    }
  }

  @Override
  public <I> Class<? extends I> getMockClass(final ClassLoader loader,
                                             final Class<I> clz,
                                             final Set<Method> methods,
                                             final PrintStream trace) {
    return getMockClass(loader, clz, getDefaultClassDefinitionStrategy(), methods, trace);
  }

  @Override
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods) {
    return getMockClass(loader, clz, definitionStrategy, methods, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(final ClassLoader loader,
                                             final Class<I> clz,
                                             final Set<Method> methods) {
    return getMockClass(loader, clz, methods, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, definitionStrategy, methods, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz,
                                             final Set<Method> methods) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, methods, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, ClassDefinitionStrategy definitionStrategy) {
    return this.getMockClass(loader, clz, definitionStrategy, MoxyEngine.ALL_METHODS, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(final ClassLoader loader, final Class<I> clz) {
    return this.getMockClass(loader, clz, MoxyEngine.ALL_METHODS, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy, PrintStream trace) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, definitionStrategy, MoxyEngine.ALL_METHODS, trace);
  }

  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz, final Set<Method> methods, final PrintStream trace) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, methods, trace);
  }

  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, definitionStrategy, MoxyEngine.ALL_METHODS, null);
  }

  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz, final PrintStream trace) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, MoxyEngine.ALL_METHODS, trace);
  }

  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods, PrintStream trace) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz, definitionStrategy, methods, trace);
  }

  @Override
  public <I> Class<? extends I> getMockClass(final Class<I> clz) {
    return this.getMockClass(MoxyEngine.class.getClassLoader(), clz);
  }

  @Override
  public boolean isMock(final Class<?> clz) {
    return clz.getAnnotation(MoxyMock.class) != null;
  }

  @Override
  public boolean isMock(final Object obj) {
    return this.isMock(obj.getClass());
  }

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
            || ((m.getModifiers() & Opcodes.ACC_PRIVATE) == 0))
        && (!(Object.class.equals(m.getDeclaringClass()) && "equals".equals(m.getName())))
        && (!(Object.class.equals(m.getDeclaringClass()) && "hashCode".equals(m.getName())));
  }

  /*
   * Gather all mock-candidate methods on the given class.
   *
   * Used by both classic and class mock engines.
   */
  Map<String, Method> gatherAllMockableMethods(final Class<?> originalClass) {
    final HashMap<String, Method> methods = new HashMap<>();

    Deque<Class<?>> queue = new ArrayDeque<>();
    Set<Method> seen = new HashSet<>();

    queue.add(originalClass);

    while (!queue.isEmpty()) {
      Class<?> current = queue.removeFirst();

      for (final Method m : current.getDeclaredMethods()) {
        String sig = m.getName() + Type.getMethodDescriptor(m);
        if (!seen.contains(m) && this.isMockCandidate(m) && !methods.containsKey(sig)) {
          seen.add(m);
          methods.put(sig, m);
        }
      }

      for (Class<?> iface : current.getInterfaces()) {
        queue.push(iface);
      }

      if ((current = current.getSuperclass()) != null) {
        queue.push(current);
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
   * (although the recorder will record their invocations separately)
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

  @Override
  public <T> MoxyStubber<T> when(final InvocationSupplier<T> invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::get);
    return new ASMMoxyStubber<>(this, invocations);
  }

  @Override
  public MoxyVoidStubber when(final InvocationRunnable invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::run);
    return new ASMMoxyVoidStubber(this, invocations);
  }

  @Override
  public MoxyVerifier assertMock(final InvocationRunnable invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::run);
    return new ASMMoxyVerifier(this, invocations);
  }

  @Override
  public MoxyMultiVerifier assertMocks(final InvocationRunnable invocation) {
    final List<Invocation> invocations = this.runMonitoredInvocation(invocation::run);
    return new ASMMoxyMultiVerifier(this, invocations);
  }

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
  <T> T instantiateMock(final Class<? extends T> mockClass) {
    if (!this.isMock(mockClass)) {
      throw new IllegalArgumentException("Cannot instantiate " + mockClass +": it is not a mock class");
    }

    try {
      final Object mock = UnsafeUtils.allocateInstance(mockClass);
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
  @SuppressWarnings({ "unchecked" })
  <T> T initializeMock(final Class<? extends T> mockClass, final Object mock) {
    try {
      final Field ivarsField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_IVARS_FIELD_NAME);
      UnsafeUtils.putObject(mock, UnsafeUtils.objectFieldOffset(ivarsField), new ASMMockInstanceVars(this));
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
      throw new MockGenerationException("Mocking of final classes is not supported with classic mocking; Try the class mock API (Moxy.mockClasses(...))");
    }

    final String clzInternalName = Type.getInternalName(clz);
    final ClassReader reader = new ClassReader(clzInternalName);

    // Find the annotated methods (and their interfaces)

    Map<String, Method> mockableMethods;
    if (methods == MoxyEngine.ALL_METHODS) {
      mockableMethods = this.gatherAllMockableMethods(clz);
    } else {
      mockableMethods = methods.stream().collect(Collectors.toMap(m -> m.getName() + Type.getMethodDescriptor(m), Function.identity()));
    }

    final AbstractMoxyTypeVisitor visitor =
        clz.isInterface() ?
            new MoxyMockInterfaceVisitor(clz, mockableMethods)  :
            new MoxyMockClassVisitor(clz, mockableMethods)      ;

    reader.accept(visitor, ClassReader.SKIP_DEBUG);

    if (trace != null) {
      visitor.getNode().accept(new TraceClassVisitor(new PrintWriter(trace)));
    }

    return visitor.getNode();
  }

  /*
   * Transform a ClassNode into bytecode.
   */
  private byte[] generateBytecode(final ClassNode node) {
    final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    final CheckClassAdapter check = new CheckClassAdapter(writer, false);
    node.accept(check);
    return writer.toByteArray();
  }

  private DefaultClassDefinitionStrategy cachedDefaultClassDefinitionStrategy;

  @Override
  public synchronized ClassDefinitionStrategy getDefaultClassDefinitionStrategy() {
    if (this.cachedDefaultClassDefinitionStrategy == null) {
      this.cachedDefaultClassDefinitionStrategy = new DefaultClassDefinitionStrategy();
    }

    return this.cachedDefaultClassDefinitionStrategy;
  }
}
