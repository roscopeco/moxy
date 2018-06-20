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

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.roscopeco.moxy.api.AnswerProvider;
import com.roscopeco.moxy.api.InvalidMockInvocationException;

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

  public default <T extends AbstractStub> void __moxy_asm_removeStubbingFrom(
                final Map<StubMethod, Deque<T>> map,
                final StubMethod stubMethod,
                final Invocation invocation) {
    final Deque<T> deque = map.get(stubMethod);

    if (deque != null) {
      final ASMMoxyMatcherEngine matchEngine = __moxy_asm_ivars().getEngine().getMatcherEngine();

      final Deque<T> newDeque = new ArrayDeque<>(deque.stream()
          .filter(obj -> !matchEngine.argsMatch(invocation.getArgs(), obj.args))
          .collect(Collectors.toList()));

      map.put(stubMethod, newDeque);
    }
  }

  // TODO move to private methods class
  public default void __moxy_asm_removePriorStubbing(final StubMethod stubMethod, final Invocation invocation) {
    __moxy_asm_removeStubbingFrom(__moxy_asm_ivars().getReturnMap(), stubMethod, invocation);
    __moxy_asm_removeStubbingFrom(__moxy_asm_ivars().getThrowMap(), stubMethod, invocation);
    __moxy_asm_removeStubbingFrom(__moxy_asm_ivars().getCallSuperMap(), stubMethod, invocation);
    __moxy_asm_removeStubbingFrom(__moxy_asm_ivars().getDelegateMap(), stubMethod, invocation);
  }

  // TODO move to private methods class
  public default <T extends AbstractStub> void __moxy_asm_safelySetStubbing(
                final Map<StubMethod, Deque<T>> map,
                final Invocation invocation,
                final T stubbing) {
    synchronized (__moxy_asm_ivars()) {
      final StubMethod stubMethod = new StubMethod(invocation.getMethodName(), invocation.getMethodDesc());
      __moxy_asm_removePriorStubbing(stubMethod, invocation);
      final Deque<T> deque = map.computeIfAbsent(stubMethod, k -> new ArrayDeque<>());
      deque.addFirst(stubbing);
    }
  }

  public default void __moxy_asm_setThrowOrReturn(final Invocation invocation,
                                                  final Object object,
                                                  final boolean isReturn) {


    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Stubbing call to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    if (isReturn) {
      final Map<StubMethod, Deque<StubReturn>> returnMap = __moxy_asm_ivars().getReturnMap();
      __moxy_asm_safelySetStubbing(returnMap, invocation, new StubReturn(invocation.getArgs(), object));
    } else {
      final Map<StubMethod, Deque<StubThrow>> throwMap = __moxy_asm_ivars().getThrowMap();
      if (object instanceof Throwable) {
        __moxy_asm_safelySetStubbing(throwMap, invocation, new StubThrow(invocation.getArgs(), (Throwable)object));
      } else {
        throw new IllegalArgumentException("Cannot throw non-Throwable class " + object.getClass().getName());
      }
    }
  }

  public default void __moxy_asm_setShouldCallSuper(final Invocation invocation,
                                                    final boolean callSuper) {
    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Stubbing call to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    final Map<StubMethod, Deque<StubSuper>> superMap = __moxy_asm_ivars().getCallSuperMap();
    __moxy_asm_safelySetStubbing(superMap, invocation, new StubSuper(invocation.getArgs(), true));
  }

  public default void __moxy_asm_setDelegateTo(final Invocation invocation,
                                               final Method method,
                                               final Object delegate) {
    final Map<StubMethod, Deque<StubDelegate>> delegateMap = __moxy_asm_ivars().getDelegateMap();
    __moxy_asm_safelySetStubbing(delegateMap, invocation, new StubDelegate(invocation.getArgs(), method, delegate));
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
      if (matchEngine.argsMatch(invocation.getArgs(), actions.args)) {
        matchingStub = actions;
        break;
      }
    }

    if (matchingStub == null) {
      list.add(new StubDoActions(invocation.getArgs(), action));
    } else {
      matchingStub.actions.add(action);
    }
  }

  public default Throwable __moxy_asm_getThrowForInvocation(final Invocation invocation) {
    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();
    final Map<StubMethod, Deque<StubThrow>> throwMap = ivars.getThrowMap();
    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final Deque<StubThrow> deque = throwMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (final StubThrow stubThrow : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubThrow.args)) {
          return stubThrow.toThrow;
        }
      }
    }

    return null;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public default Object __moxy_asm_getReturnForInvocation(final Invocation invocation) {
    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();
    final Map<StubMethod, Deque<StubReturn>> returnMap = ivars.getReturnMap();
    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final Deque<StubReturn> deque = returnMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (final StubReturn stubReturn : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubReturn.args)) {
          final Object toReturn = stubReturn.toReturn;

          if (toReturn instanceof AnswerProvider) {
            final List<? extends Object> immutableArgs =
                Collections.unmodifiableList(invocation.getArgs());

            return ((AnswerProvider)toReturn).provide(immutableArgs);
          } else {
            return stubReturn.toReturn;
          }
        }
      }
    }

    return null;
  }

  public default boolean __moxy_asm_shouldCallSuperForInvocation(final Invocation invocation) {
    if (invocation == null) {
      throw new InvalidMockInvocationException("[BUG] Mock callback to support with no recorded invocation\n"
                                             + "(If you're testing the framework; may indicate an incomplete partial mock engine)");
    }

    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();

    final Map<StubMethod, Deque<StubSuper>> superMap = ivars.getCallSuperMap();
    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final Deque<StubSuper> deque = superMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (final StubSuper stubSuper : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubSuper.args)) {
          return stubSuper.callSuper;
        }
      }
    }

    return false;
  }

  // ThreadLocal Cache for the stubdelegate - saves two lookups, argsmatch, etc.
  //
  // NOTE: This relies on generated code delegating immediately
  // if shouldDelegateForInvocation is true!
  //
  // It is subsequently cleared in runDelegateForInvocation...
  static final ThreadLocal<StubDelegate> stubDelegateCache = new ThreadLocal<>();

  public default boolean __moxy_asm_shouldDelegateForInvocation(final Invocation invocation) {
    final ASMMockInstanceVars ivars = this.__moxy_asm_ivars();
    final Map<StubMethod, Deque<StubDelegate>> delegateMap = ivars.getDelegateMap();
    final ASMMoxyMatcherEngine matchEngine = ivars.getEngine().getMatcherEngine();
    final Deque<StubDelegate> deque = delegateMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (final StubDelegate stubDelegate : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubDelegate.args)) {
          stubDelegateCache.set(stubDelegate);
          return true;
        }
      }
    }

    return false;
  }

  public default Object __moxy_asm_runCachedDelegate() {
    final StubDelegate stubDelegate = stubDelegateCache.get();

    if (stubDelegate.equals(null)) {
      throw new IllegalStateException("[BUG] Attempted to run cached delegate, but cache was empty");
    }

    try {
      return stubDelegate.invoke();
    } finally {
      stubDelegateCache.set(null);
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
        if (matchEngine.argsMatch(invocation.getArgs(), stubDoActions.args)) {
          stubDoActions.actions.forEach(action -> action.accept(immutableArgs));
        }
      }
    }
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default Throwable __moxy_asm_getThrowForCurrentInvocation() {
    return __moxy_asm_getThrowForInvocation(
        __moxy_asm_ivars().getEngine().getRecorder().getCurrentInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default Object __moxy_asm_getReturnForCurrentInvocation() {
    return __moxy_asm_getReturnForInvocation(
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
