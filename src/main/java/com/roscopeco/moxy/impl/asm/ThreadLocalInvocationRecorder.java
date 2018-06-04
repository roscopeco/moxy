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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.matchers.MoxyMatcher;

public class ThreadLocalInvocationRecorder {
  private final ASMMoxyEngine engine;

  private final ThreadLocal<LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>>
      invocationMapThreadLocal;

  private final ThreadLocal<Invocation> lastInvocationThreadLocal;

  ThreadLocalInvocationRecorder(final ASMMoxyEngine engine) {
    this.engine = engine;
    this.invocationMapThreadLocal = new ThreadLocal<>();
    this.lastInvocationThreadLocal = new ThreadLocal<>();
  }

  /*
   * This gets any matchers from the MatcherEngine's stack, and replaces
   * the arguments from the last invocation with them.
   */
  void replaceInvocationArgsWithMatchers() {
    final ASMMoxyMatcherEngine mengine = this.engine.getASMMatcherEngine();
    final List<MoxyMatcher<?>> matchers = mengine.popMatchers();
    if (matchers != null) {
      final Invocation lastInvocation = this.getLastInvocation();

      final List<Object> lastArgs = lastInvocation.getArgs();
      if (lastArgs.size() != matchers.size()) {
        throw new InconsistentMatchersException(lastArgs.size(), mengine.ensureMatcherStack());
      } else {
        for (int i = 0; i < lastArgs.size(); i++) {
          lastArgs.set(i, matchers.get(i));
        }
      }
    }
  }

  // public as it's accessed from mocks (in different packages).
  public void recordInvocation(final Object receiver,
                               final String methodName,
                               final String methodDesc,
                               final List<Object> args) {

    final List<Invocation> invocations = this.ensureInvocationList(
        this.ensureInvocationMap(this.ensureLocalClassMap(), receiver.getClass()),
        methodName, methodDesc);

    final Invocation invocation = new Invocation(receiver,
                                           methodName,
                                           methodDesc,
                                           args);

    // Add to list of invocations
    invocations.add(invocation);

    // Record last invocation on this thread
    this.lastInvocationThreadLocal.set(invocation);

    // Fixup matchers
    this.replaceInvocationArgsWithMatchers();
  }

  /*
   * NOTE This does exactly what it says - deletes the invocation from the
   * list, but not from the last invocation thread local. This means the engine
   * can still use the last invocation for the args.
   *
   * @see comments on {@link MoxyInvocationRecorder#unrecordLastInvocation}.
   */
  void unrecordLastInvocation() {
    final Invocation lastInvocation = this.getLastInvocation();

    if (lastInvocation != null) {
      final List<Invocation> invocations = this.ensureInvocationList(
          this.ensureInvocationMap(this.ensureLocalClassMap(),
                              lastInvocation.getReceiver().getClass()),
                              lastInvocation.getMethodName(),
                              lastInvocation.getMethodDesc());

          invocations.remove(invocations.size() - 1);
    }
  }

  List<Invocation> getInvocationList(final Class<?> forClz, final String methodName, final String methodDesc) {
    return this.ensureInvocationList(this.ensureInvocationMap(this.ensureLocalClassMap(), forClz), methodName, methodDesc);
  }

  Invocation getLastInvocation() {
    return this.lastInvocationThreadLocal.get();
  }

  void clearLastInvocation() {
    this.lastInvocationThreadLocal.set(null);
  }

  Invocation getAndClearLastInvocation() {
    final Invocation invocation = this.getLastInvocation();
    this.clearLastInvocation();
    return invocation;
  }

  void reset() {
    this.lastInvocationThreadLocal.set(null);
    this.invocationMapThreadLocal.set(null);
  }

  private LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> ensureLocalClassMap() {
    LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>
        classMap = this.invocationMapThreadLocal.get();

    if (classMap == null) {
      classMap = new LinkedHashMap<>();
      this.invocationMapThreadLocal.set(classMap);
    }

    return classMap;
  }

  private LinkedHashMap<String, List<Invocation>> ensureInvocationMap(
      final LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> classMap,
      final Class<?> forClz) {
    return classMap.computeIfAbsent(forClz, k -> new LinkedHashMap<>());
  }

  private List<Invocation> ensureInvocationList(
      final LinkedHashMap<String, List<Invocation>> invocationMap,
      final String forMethodName, final String forMethodDescriptor) {
    return invocationMap.computeIfAbsent(forMethodName + forMethodDescriptor,
        k -> new ArrayList<>());
  }
}
