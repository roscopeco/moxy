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
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.impl.asm.ASMMockInstanceVars.CachedDelegate;
import com.roscopeco.moxy.impl.asm.stubs.Stub;
import com.roscopeco.moxy.impl.asm.stubs.StubDoActions;
import com.roscopeco.moxy.impl.asm.stubs.StubInvocation;
import com.roscopeco.moxy.impl.asm.stubs.StubMethod;
import com.roscopeco.moxy.impl.asm.stubs.StubType;

/**
 * All mocks implement this interface. It (ab)uses default methods
 * to allow us to do less bytecode generation.
 *
 * Don't implement this yourself, it's only public because it needs
 * to be visible to subclasses.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 *
 */
public interface ASMMockSupport {
  /* For mocks to implement */
  public ASMMockInstanceVars __moxy_asm_ivars();

  /* Implemented by default */
  public default ThreadLocalInvocationRecorder __moxy_asm_getRecorder() {
    return __moxy_asm_ivars().getEngine().getRecorder();
  }

  public default String __moxy_asm_makeJavaSignature(final String name, final String desc) {
    return TypeStringUtils.javaMethodSignature(name, desc);
  }

  public default void __moxy_asm_removePriorStubbing(final Invocation invocation) {
    this.__moxy_asm_removePriorStubbing(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()),
        invocation);
  }

  public default void __moxy_asm_removePriorStubbing(final StubMethod stubMethod,
                                                     final Invocation invocation) {
    synchronized (__moxy_asm_ivars()) {
      final Map<StubMethod, Deque<StubInvocation>> map = __moxy_asm_ivars().getStubsMap();
      map.computeIfPresent(stubMethod, (k, deque) -> {
        final ASMMoxyMatcherEngine matchEngine = __moxy_asm_ivars().getEngine().getMatcherEngine();

        return new ArrayDeque<>(deque.stream()
            .filter(stubInvocation -> !matchEngine.argsMatch(invocation.getArgs(), stubInvocation.getArgs()))
            .collect(Collectors.toList()));
      });
    }
  }

  // TODO move to private methods class
  //
  // NOTE: This should be used when the arguments come from a monitored
  // invocation (i.e. may contain matchers).
  public default StubInvocation findStubInvocationForMonitoredArgs(final Deque<StubInvocation> deque,
                                                                   final List<Object> args) {
    for (final StubInvocation invocation : deque) {
      if (args.equals(invocation.getArgs())) {
        return invocation;
      }
    }

    return null;
  }

  // TODO move to private methods class
  public default void __moxy_asm_safelyAddStubbing(
                final Map<StubMethod, Deque<StubInvocation>> map,
                final Invocation invocation,
                final Stub stubbing) {
    synchronized (__moxy_asm_ivars()) {
      final StubMethod stubMethod = new StubMethod(invocation.getMethodName(), invocation.getMethodDesc());
      final Deque<StubInvocation> deque = map.computeIfAbsent(stubMethod, k -> new ArrayDeque<>());

      StubInvocation stubInvocation = findStubInvocationForMonitoredArgs(deque, invocation.getArgs());

      if (stubInvocation != null) {
        stubInvocation.getStubs().addLast(stubbing);
      } else {
        stubInvocation = new StubInvocation(invocation.getArgs());
        stubInvocation.getStubs().addFirst(stubbing);
        deque.addLast(stubInvocation);
      }
    }
  }

  public default void __moxy_asm_setStubbing(final Invocation invocation,
                                             final Stub stub) {

    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Stubbing call to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    final Map<StubMethod, Deque<StubInvocation>> stubsMap = __moxy_asm_ivars().getStubsMap();

    __moxy_asm_safelyAddStubbing(stubsMap, invocation, stub);
  }

  public default void __moxy_asm_addDoAction(final Invocation invocation,
                                             final Consumer<List<? extends Object>> action) {
    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Stubbing call to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();

    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final Map<StubMethod, List<StubDoActions>> doActionsMap = ivars.getDoActionsMap();
    final List<StubDoActions> list = doActionsMap.computeIfAbsent(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()),
        k -> new ArrayList<>());

    StubDoActions matchingStub = null;

    for (final StubDoActions actions : list) {
      if (matchEngine.argsMatch(invocation.getArgs(), actions.getMatchArgs())) {
        matchingStub = actions;
        break;
      }
    }

    if (matchingStub == null) {
      list.add(new StubDoActions(invocation.getArgs(), action));
    } else {
      matchingStub.getActions().add(action);
    }
  }

  // NOTE: This should NOT be used when the arguments come from a monitored
  // invocation (i.e. may contain matchers).
  //
  // In that case, use #findStubInvocationForMonitoredArgs instead.
  public default StubInvocation findStubbingForActualInvocation(final Invocation invocation) {
    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Mock callback to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();
    final Map<StubMethod, Deque<StubInvocation>> stubsMap = ivars.getStubsMap();
    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final Deque<StubInvocation> deque = stubsMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (final StubInvocation stubInvocation : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubInvocation.getArgs())) {
          return stubInvocation;
        }
      }
    }

    return null;
  }

  /*
   * Get throw for next invocation, or null if next stub isn't a throw.
   */
  public default Throwable __moxy_asm_getThrowableForInvocation(final Invocation invocation) {
    final StubInvocation stubInvocation = findStubbingForActualInvocation(invocation);

    if (stubInvocation != null) {
      final Stub nextStub = stubInvocation.getStubs().peek();
      if (nextStub != null && nextStub.getType().equals(StubType.THROW_EXCEPTION)) {
        if (nextStub.isRetained() || stubInvocation.getStubs().size() < 2) {
          return (Throwable)nextStub.getObject(invocation.getArgs());
        } else {
          return (Throwable)stubInvocation.getStubs().pop().getObject(invocation.getArgs());
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public default Object __moxy_asm_getReturnableForInvocation(final Invocation invocation) {
    final StubInvocation stubInvocation = findStubbingForActualInvocation(invocation);

    if (stubInvocation != null) {
      final Stub nextStub = stubInvocation.getStubs().peek();
      if (nextStub != null && nextStub.getType().equals(StubType.RETURN_OBJECT)) {
        if (nextStub.isRetained() || stubInvocation.getStubs().size() < 2) {
          return nextStub.getObject(invocation.getArgs());
        } else {
          return stubInvocation.getStubs().pop().getObject(invocation.getArgs());
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public default boolean __moxy_asm_shouldCallSuperForInvocation(final Invocation invocation) {
    final StubInvocation stubInvocation = findStubbingForActualInvocation(invocation);

    if (stubInvocation != null) {
      final Stub nextStub = stubInvocation.getStubs().peek();
      if (nextStub != null && nextStub.getType().equals(StubType.CALL_SUPER)) {
        if (!nextStub.isRetained() && stubInvocation.getStubs().size() > 1) {
          stubInvocation.getStubs().pop();
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public default boolean __moxy_asm_shouldDelegateForInvocation(final Invocation invocation) {
    final StubInvocation stubInvocation = findStubbingForActualInvocation(invocation);
    final ASMMockInstanceVars ivars = __moxy_asm_ivars();

    if (stubInvocation != null) {
      final Stub nextStub = stubInvocation.getStubs().peek();
      if (nextStub != null && nextStub.getType().equals(StubType.DELEGATE)) {
        if (!nextStub.isRetained() && stubInvocation.getStubs().size() > 1) {
          stubInvocation.getStubs().pop();
        }

        ivars.getStubDelegateCache().set(
            ivars.new CachedDelegate(nextStub, invocation.getArgs()));
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public default Object __moxy_asm_runCachedDelegate() {
    final CachedDelegate stubDelegate = __moxy_asm_ivars().getStubDelegateCache().get();

    if (stubDelegate == null) {
      throw new IllegalStateException("[BUG] Attempted to run cached delegate, but cache was empty");
    }

    try {
      return stubDelegate.delegate.getObject(stubDelegate.actualArgs);
    } finally {
      __moxy_asm_ivars().getStubDelegateCache().set(null);
    }
  }


  public default void __moxy_asm_runDoActionsForInvocation(final Invocation invocation) {
    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Mock callback to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();
    final Map<StubMethod, List<StubDoActions>> superMap = ivars.getDoActionsMap();
    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final List<StubDoActions> list = superMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    //
    // Note that this runs _all_ actions that match the arguments. If using matchers,
    // this could run ones that weren't necessarily intended. This needs to be documented!
    final List<? extends Object> immutableArgs =
        Collections.unmodifiableList(invocation.getArgs());

    if (list != null) {
      for (final StubDoActions stubDoActions : list) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubDoActions.getMatchArgs())) {
          stubDoActions.getActions().forEach(action -> action.accept(immutableArgs));
        }
      }
    }
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default Throwable __moxy_asm_getThrowableForCurrentInvocation() {
    return __moxy_asm_getThrowableForInvocation(
        __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default Object __moxy_asm_getReturnableForCurrentInvocation() {
    return __moxy_asm_getReturnableForInvocation(
        __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default boolean __moxy_asm_shouldCallSuperForCurrentInvocation() {
    return __moxy_asm_shouldCallSuperForInvocation(
        __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default boolean __moxy_asm_shouldDelegateForCurrentInvocation() {
    return __moxy_asm_shouldDelegateForInvocation(
        __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default void __moxy_asm_runDoActionsForCurrentInvocation() {
    __moxy_asm_runDoActionsForInvocation(
        __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default void __moxy_asm_updateCurrentInvocationReturnThrow(final Object returned, final Throwable threw) {
    final Invocation invocation = __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation();
    invocation.setReturned(returned);
    invocation.setThrew(threw);
  }

  public default boolean __moxy_asm_isMockBehaviourDisabledOnThisThread() {
    return __moxy_asm_ivars().getEngine().isMockStubbingDisabledOnThisThread();
  }
}
