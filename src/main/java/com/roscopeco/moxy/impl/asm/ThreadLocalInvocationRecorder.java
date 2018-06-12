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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.matchers.MoxyMatcher;

public class ThreadLocalInvocationRecorder {
  private final ASMMoxyEngine engine;

  /*
   * This list keeps track of all 'standard' (i.e. unmonitored) invocations,
   * keyed by mock class, then by method for faster searching in whens and single-invocation
   * verifiers.
   */
  private final ThreadLocal<HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>>
      invocationMapThreadLocal;

  /*
   * This list keeps track of all 'standard' (i.e. unmonitored) invocations.
   * These are invocations that do not occur in a when or assert.
   *
   * This is used by the multi-invocation verifiers.
   */
  private final ThreadLocal<ArrayList<Invocation>> standardInvocationsOrderedList;

  /*
   * This stores the current invocation, and is valid *only* during invocation
   * of mocked methods. It is cleared before mocks return.
   */
  private final ThreadLocal<Invocation> currentInvocationThreadLocal;

  /*
   * This stack keeps track of monitored invocations. Each time a monitored
   * invocation is started, a new 'frame' is pushed, and used at the end of
   * that invocation to retrieve the monitored invocations.
   *
   * It's a stack in case someone feels the need to nest when/asserts.
   */
  private final ThreadLocal<ArrayDeque<ArrayList<Invocation>>> monitoredInvocationStackThreadLocal;

  ThreadLocalInvocationRecorder(final ASMMoxyEngine engine) {
    this.engine = engine;
    this.invocationMapThreadLocal = new ThreadLocal<>();
    this.standardInvocationsOrderedList = new ThreadLocal<>();
    this.currentInvocationThreadLocal = new ThreadLocal<>();
    this.monitoredInvocationStackThreadLocal = new ThreadLocal<>();
    this.monitoredInvocationStackThreadLocal.set(new ArrayDeque<>());
  }

  /*
   * This gets any matchers from the MatcherEngine's stack, and replaces
   * the arguments from the last invocation with them.
   *
   * This is called from the mock itself, so we're guaranteed to have
   * an invocation at this point.
   */
  void replaceInvocationArgsWithMatchers(final Invocation invocation) {
    final ASMMoxyMatcherEngine mengine = this.engine.getMatcherEngine();
    final List<MoxyMatcher<?>> matchers = mengine.popMatchers();
    if (matchers != null) {
      final List<Object> lastArgs = invocation.getArgs();
      if (lastArgs.size() != matchers.size()) {
        throw new InconsistentMatchersException(lastArgs.size(), mengine.getMatcherStack());
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

    // Fixup matchers
    this.replaceInvocationArgsWithMatchers(invocation);

    if (this.monitoredInvocationStackThreadLocal.get().isEmpty()) {
      // Not in a monitored invocation, add to standard map/list
      final List<Invocation> orderedInvocations =
          this.ensureAllInvocationsOrderedList();

      // Add to list of invocations mapped by class (for faster lookup)
      invocations.add(invocation);

      // Add to ordered list (for in-order verification)
      orderedInvocations.add(invocation);
    } else {
      // In a monitored invocation, just add to list at top of stack.
      final List<Invocation> orderedInvocations =
          this.monitoredInvocationStackThreadLocal.get().peek();

      orderedInvocations.add(invocation);
    }

    // Record current invocation on this thread.
    // Mocks rely on this to set their throws/returns,
    // so must always be set!
    this.currentInvocationThreadLocal.set(invocation);
  }

  /*
   * NOTE This does exactly what it says - deletes the invocation from the
   * lists, but not from the last invocation thread local. This means the engine
   * can still use the last invocation for the args.
   *
   * @see comments on {@link MoxyInvocationRecorder#unrecordLastInvocation}.
   */
  void unrecordLastInvocation() {
    final Invocation lastInvocation = this.getCurrentInvocation();

    if (lastInvocation != null) {
      final List<Invocation> invocations = this.ensureInvocationList(
          this.ensureInvocationMap(this.ensureLocalClassMap(),
                              lastInvocation.getReceiver().getClass()),
                              lastInvocation.getMethodName(),
                              lastInvocation.getMethodDesc());

          invocations.remove(invocations.size() - 1);
    }
  }

  /*
   * Get invocations for the given class/method/desc combo, in order.
   *
   * Returns a copy of the original list.
   */
  List<Invocation> getInvocationList(final Class<?> forClz, final String methodName, final String methodDesc) {
    return new ArrayList<>(this.ensureInvocationList(this.ensureInvocationMap(this.ensureLocalClassMap(), forClz), methodName, methodDesc));
  }

  /*
   * Get _all_ invocations, in order.
   *
   * Returns a copy of the original list.
   */
  List<Invocation> getInvocationList() {
    return new ArrayList<>(this.ensureAllInvocationsOrderedList());
  }

  /*
   * Get the current invocation. Valid *only* during a mock invocation.
   */
  Invocation getCurrentInvocation() {
    return this.currentInvocationThreadLocal.get();
  }

  void reset() {
    this.currentInvocationThreadLocal.set(null);
    this.standardInvocationsOrderedList.set(null);
    this.invocationMapThreadLocal.set(null);
    this.standardInvocationsOrderedList.set(null);
  }

  void startMonitoredInvocation() {
    this.monitoredInvocationStackThreadLocal.get().push(new ArrayList<>());
  }

  List<Invocation> getCurrentMonitoredInvocations() {
    return this.monitoredInvocationStackThreadLocal.get().peek();
  }

  void endMonitoredInvocation() {
    if (this.monitoredInvocationStackThreadLocal.get().isEmpty()) {
      throw new IllegalStateException("[BUG] Attempt to end an unstarted monitored invocation (in recorder)");
    }

    this.currentInvocationThreadLocal.set(null);
    this.monitoredInvocationStackThreadLocal.get().pop();
  }

  private HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> ensureLocalClassMap() {
    HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>
        classMap = this.invocationMapThreadLocal.get();

    if (classMap == null) {
      classMap = new HashMap<>();
      this.invocationMapThreadLocal.set(classMap);
    }

    return classMap;
  }

  private List<Invocation> ensureAllInvocationsOrderedList() {
    ArrayList<Invocation> list = this.standardInvocationsOrderedList.get();

    if (list == null) {
      list = new ArrayList<>();
      this.standardInvocationsOrderedList.set(list);
    }

    return list;
  }

  private LinkedHashMap<String, List<Invocation>> ensureInvocationMap(
      final HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> classMap,
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
