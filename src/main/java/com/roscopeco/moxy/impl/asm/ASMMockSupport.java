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
import java.util.Deque;
import java.util.Map;

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
  public ASMMoxyEngine __moxy_asm_getEngine();

  // Using Deque here for efficient add-at-front, so when
  // we stream we see the most recent stubbing...
  public Map<StubMethod, Deque<StubReturn>> __moxy_asm_getReturnMap();
  public Map<StubMethod, Deque<StubThrow>> __moxy_asm_getThrowMap();

  // Present and true: call super; otherwise, use stubbing.
  public Map<StubMethod, Deque<StubSuper>> __moxy_asm_getCallSuperMap();

  /* Implemented by default */
  public default ThreadLocalInvocationRecorder __moxy_asm_getRecorder() {
    return __moxy_asm_getEngine().getRecorder();
  }

  public default Deque<StubReturn>__moxy_asm_ensureStubReturnDeque(
      final Map<StubMethod, Deque<StubReturn>> returnMap,
      final StubMethod method) {
    return returnMap.computeIfAbsent(method, k -> new ArrayDeque<>());
  }

  public default Deque<StubThrow>__moxy_asm_ensureStubThrowDeque(
      final Map<StubMethod, Deque<StubThrow>> throwMap,
      final StubMethod method) {
    return throwMap.computeIfAbsent(method, k -> new ArrayDeque<>());
  }

  public default Deque<StubSuper>__moxy_asm_ensureStubSuperDeque(
      final Map<StubMethod, Deque<StubSuper>> superMap,
      final StubMethod method) {
    return superMap.computeIfAbsent(method, k-> new ArrayDeque<>());
  }

  public default String __asm_moxy_makeJavaSignature(final String name, final String desc) {
    return TypeStringUtils.javaMethodSignature(name, desc);
  }

  public default void __moxy_asm_setThrowOrReturn(final Invocation invocation,
                                                  final Object object,
                                                  final boolean isReturn) {


    if (isReturn) {
      if (__moxy_asm_getThrowForInvocation(invocation) != null
          || __moxy_asm_shouldCallSuperForInvocation(invocation)) {
        throw new IllegalStateException("Cannot set return for '"
                                        + TypeStringUtils.javaMethodSignature(invocation)
                                        + "' "
                                        + TypeStringUtils.buildArgsString(invocation)
                                        + "as it has already been stubbed to throw or call real method");
      } else {
        final Map<StubMethod, Deque<StubReturn>> returnMap = __moxy_asm_getReturnMap();
        final Deque<StubReturn> deque = __moxy_asm_ensureStubReturnDeque(
            returnMap,
            new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));
        deque.addFirst(new StubReturn(invocation.getArgs(), object));
      }
    } else {
      if (__moxy_asm_getReturnForInvocation(invocation) != null
          || __moxy_asm_shouldCallSuperForInvocation(invocation)) {
        throw new IllegalStateException("Cannot set throw for '"
                                        + TypeStringUtils.javaMethodSignature(invocation)
                                        + "' "
                                        + TypeStringUtils.buildArgsString(invocation)
                                        + "as it has already been stubbed to return or call real method");
      } else {
        final Map<StubMethod, Deque<StubThrow>> throwMap = __moxy_asm_getThrowMap();
        final Deque<StubThrow> deque = __moxy_asm_ensureStubThrowDeque(
            throwMap,
            new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

        if (object instanceof Throwable) {
          deque.addFirst(new StubThrow(invocation.getArgs(), (Throwable)object));
        } else {
          throw new IllegalArgumentException("Cannot throw non-Throwable class " + object.getClass().getName());
        }
      }
    }
  }

  public default void __moxy_asm_setShouldCallSuper(final Invocation invocation,
                                                    final boolean callSuper) {
    if (__moxy_asm_getThrowForInvocation(invocation) != null
        || __moxy_asm_getReturnForInvocation(invocation) != null) {
      throw new IllegalStateException("Cannot call real method for '"
                                    + TypeStringUtils.javaMethodSignature(invocation)
                                    + "' "
                                    + TypeStringUtils.buildArgsString(invocation)
                                    + "as it has already been stubbed");
    } else {
      final Map<StubMethod, Deque<StubSuper>> superMap = __moxy_asm_getCallSuperMap();
      final Deque<StubSuper> deque = __moxy_asm_ensureStubSuperDeque(
          superMap,
          new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));
      deque.addFirst(new StubSuper(invocation.getArgs(), true));
    }
  }

  public default Throwable __moxy_asm_getThrowForInvocation(final Invocation invocation) {
    final Map<StubMethod, Deque<StubThrow>> throwMap = __moxy_asm_getThrowMap();
    final ASMMoxyMatcherEngine matchEngine = this.__moxy_asm_getEngine().getASMMatcherEngine();
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

  public default Object __moxy_asm_getReturnForInvocation(final Invocation invocation) {
    final Map<StubMethod, Deque<StubReturn>> returnMap = __moxy_asm_getReturnMap();
    final ASMMoxyMatcherEngine matchEngine = this.__moxy_asm_getEngine().getASMMatcherEngine();
    final Deque<StubReturn> deque = returnMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));

    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (final StubReturn stubReturn : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubReturn.args)) {
          return stubReturn.toReturn;
        }
      }
    }

    return null;
  }

  public default boolean __moxy_asm_shouldCallSuperForInvocation(final Invocation invocation) {
    final Map<StubMethod, Deque<StubSuper>> superMap = __moxy_asm_getCallSuperMap();
    final ASMMoxyMatcherEngine matchEngine = this.__moxy_asm_getEngine().getASMMatcherEngine();
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

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default Throwable __moxy_asm_getThrowForCurrentInvocation() {
    return __moxy_asm_getThrowForInvocation(
        __moxy_asm_getEngine().getRecorder().getLastInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default Object __moxy_asm_getReturnForCurrentInvocation() {
    return __moxy_asm_getReturnForInvocation(
        __moxy_asm_getEngine().getRecorder().getLastInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default boolean __moxy_asm_shouldCallSuperForCurrentInvocation() {
    return __moxy_asm_shouldCallSuperForInvocation(
        __moxy_asm_getEngine().getRecorder().getLastInvocation());
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current
   * invocation just prior to throw or return.
   */
  public default void __moxy_asm_updateCurrentInvocationReturnThrow(final Object returned, final Throwable threw) {
    final Invocation invocation = __moxy_asm_getEngine().getRecorder().getLastInvocation();
    invocation.setReturned(returned);
    invocation.setThrew(threw);
  }
}
