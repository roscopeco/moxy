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
import java.util.HashMap;
import java.util.HashSet;
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
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.api.MoxyMatcherEngine;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.api.MoxyVoidStubber;
import com.roscopeco.moxy.impl.asm.visitors.AbstractMoxyTypeVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockClassVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockInterfaceVisitor;
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
  private static final String UNRECOVERABLE_ERROR = "Unrecoverable Error";
  private static final String CANNOT_MOCK_NULL_CLASS = "Cannot mock null class";

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

  public ASMMoxyEngine() {
    this.recorder = new ThreadLocalInvocationRecorder(this);
    this.matcherEngine = new ASMMoxyMatcherEngine(this);
  }

  ASMMoxyEngine(final ThreadLocalInvocationRecorder recorder, final ASMMoxyMatcherEngine matcherEngine) {
    this.recorder = recorder;
    this.matcherEngine = matcherEngine;
  }

  public ThreadLocalInvocationRecorder getRecorder() {
    return this.recorder;
  }

  @Override
  public MoxyMatcherEngine getMatcherEngine() {
    return this.matcherEngine;
  }

  ASMMoxyMatcherEngine getASMMatcherEngine() {
    return this.matcherEngine;
  }

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

  @Override
  public boolean isMock(final Class<?> clz) {
    return clz.getAnnotation(Mock.class) != null;
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

  boolean isMockCandidate(final Method m) {
    return ((m.getModifiers() & Opcodes.ACC_FINAL) == 0)
        && (((m.getModifiers() & Opcodes.ACC_PUBLIC) > 0)
            || ((m.getModifiers() & Opcodes.ACC_PROTECTED) > 0)
            || ((m.getModifiers() & Opcodes.ACC_PRIVATE) == 0));
  }

  HashSet<Method> gatherAllMockableMethods(final Class<?> originalClass) {
    final HashSet<Method> methods = new HashSet<>();

    for (final Method m : originalClass.getDeclaredMethods()) {
      if (this.isMockCandidate(m)) {
        methods.add(m);
      }
    }

    return methods;
  }

  void ensureEngineConsistencyBeforeMonitoredInvocation() {
    this.getRecorder().clearLastInvocation();
    this.getASMMatcherEngine().validateStackConsistency();
  }

  /*
   * Validates matcher stack consistency.
   */
  void naivelyInvokeAndSwallowExceptions(final InvocationRunnable doInvoke) {
    this.ensureEngineConsistencyBeforeMonitoredInvocation();
    try {
      doInvoke.run();
    } catch (final MoxyException e) {
      // Framework error - rethrow this.
      throw e;
    } catch (final NullPointerException e) {
      // Often an autoboxing error, give (hopefully) useful error message.
      throw new PossibleMatcherUsageError(
          "NPE in invocation: If you're using primitive matchers, ensure you're using the "
        + "correct type (e.g. anyInt() rather than any()), especially when nesting.\n"
        + "Otherwise, the causing exception may have more information.", e);
    } catch (final Exception e) {
      // naively swallow everything else.
    }
  }

  /*
   * Validates matcher stack consistency.
   */
  <T> void naivelyInvokeAndSwallowExceptions(final InvocationSupplier<T> doInvoke) {
    this.ensureEngineConsistencyBeforeMonitoredInvocation();
    try {
      doInvoke.get();
    } catch (final MoxyException e) {
      // Framework error - rethrow this.
      throw e;
    } catch (final NullPointerException e) {
      // Often an autoboxing error, give (hopefully) useful error message.
      throw new PossibleMatcherUsageError(
          "NPE in invocation: If you're using primitive matchers, ensure you're using the "
        + "correct type (e.g. anyInt() rather than any()), especially when nesting.\n"
        + "Otherwise, the causing exception may have more information.", e);
    } catch (final Exception e) {
      // naively swallow everything else.
    }
  }

  /*
   * Deletes latest invocation, and validates matcher stack consistency.
   */
  void deleteLatestInvocationFromList() {
    this.getRecorder().unrecordLastInvocation();
    this.getASMMatcherEngine().validateStackConsistency();
  }

  @Override
  public <T> MoxyStubber<T> when(final InvocationSupplier<T> invocation) {
    this.naivelyInvokeAndSwallowExceptions(invocation);
    this.getRecorder().replaceInvocationArgsWithMatchers();
    this.deleteLatestInvocationFromList();
    return new ASMMoxyStubber<>(this);
  }

  @Override
  public MoxyVoidStubber when(final InvocationRunnable invocation) {
    this.naivelyInvokeAndSwallowExceptions(invocation);
    this.getRecorder().replaceInvocationArgsWithMatchers();
    this.deleteLatestInvocationFromList();
    return new ASMMoxyVoidStubber(this);
  }

  @Override
  public MoxyVerifier assertMock(final InvocationRunnable invocation) {
    this.naivelyInvokeAndSwallowExceptions(invocation);
    this.getRecorder().replaceInvocationArgsWithMatchers();
    this.deleteLatestInvocationFromList();
    return new ASMMoxyVerifier(this);
  }

  @SuppressWarnings("restriction")
  <T> T instantiateMock(final Class<? extends T> mockClass) {
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
   * Instantiate a mock of the given class
   */
  @SuppressWarnings({ "unchecked", "restriction", "rawtypes" })
  <T> T initializeMock(final Class<? extends T> mockClass, final Object mock) {
    try {
      final Field engineField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_ENGINE_FIELD_NAME);
      final Field returnMapField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_RETURNMAP_FIELD_NAME);
      final Field throwMapField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_THROWMAP_FIELD_NAME);
      final Field superMapField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_SUPERMAP_FIELD_NAME);
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(engineField), this);
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(returnMapField), new HashMap());
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(throwMapField), new HashMap());
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(superMapField), new HashMap());
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
