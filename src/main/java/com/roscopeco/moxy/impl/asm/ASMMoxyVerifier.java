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

import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.MoxyVerifier;

class ASMMoxyVerifier extends HasEngineAndInvocation implements MoxyVerifier {
  private static final String EXPECTED_MOCK = "Expected mock ";
  private static final String TO_BE_CALLED = " to be called ";
  private static final String BUT_IT_WAS_CALLED = ", but it was called ";

  public ASMMoxyVerifier(final ASMMoxyEngine engine) {
    super(engine);
  }

  private String readableTimes(final int times) {
    if (times == 0) {
      return "zero times";
    } else if (times == 1) {
      return "once";
    } else if (times == 2) {
      return "twice";
    } else {
      return "" + times + " times";
    }
  }

  @Override
  public MoxyVerifier wasCalled() {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();

    if (this.getEngine()
            .getRecorder()
            .getInvocationList(invocation.getReceiver().getClass(),
                               methodName,
                               methodDesc)
        .stream()
        .anyMatch(e -> this.getEngine()
                             .getASMMatcherEngine()
                             .argsMatch(e.getArgs(), invocation.getArgs())
    )) {
      return this;
    } else {
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + TO_BE_CALLED
              + TypeStringUtils.buildArgsString(invocation)
              + "at least once but it wasn't called at all");
    }
  }

  int getCallCount(final Invocation invocation, final String methodName, final String methodDesc) {
    return this.getEngine()
        .getRecorder()
        .getInvocationList(invocation.getReceiver().getClass(),
                           methodName,
                           methodDesc)
    .stream()
    .filter(e -> this.getEngine()
                        .getASMMatcherEngine()
                        .argsMatch(e.getArgs(), invocation.getArgs()) )
    .collect(Collectors.toList())
    .size();
  }

  @Override
  public MoxyVerifier wasCalled(final int times) {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    final int actual = this.getCallCount(invocation, methodName, methodDesc);

    if (actual == times) {
      return this;
    } else {
      // TODO Stringbuilder...... on both of these (above).
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + TO_BE_CALLED
              + TypeStringUtils.buildArgsString(invocation)
              + "exactly " + this.readableTimes(times) + BUT_IT_WAS_CALLED
              + this.readableTimes(actual));
    }
  }

  @Override
  public MoxyVerifier wasNotCalled() {
    return this.wasCalled(0);
  }

  @Override
  public MoxyVerifier wasCalledOnce() {
    return this.wasCalled(1);
  }

  @Override
  public MoxyVerifier wasCalledTwice() {
    return this.wasCalled(2);
  }

  @Override
  public MoxyVerifier wasCalledAtLeast(final int times) {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    final int actual = this.getCallCount(invocation, methodName, methodDesc);

    if (actual >= times) {
      return this;
    } else {
      // TODO Stringbuilder...... on both of these (above).
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + TO_BE_CALLED
              + TypeStringUtils.buildArgsString(invocation)
              + "at least " + this.readableTimes(times) + BUT_IT_WAS_CALLED
              + this.readableTimes(actual));
    }
  }

  @Override
  public MoxyVerifier wasCalledAtMost(final int times) {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    final int actual = this.getCallCount(invocation, methodName, methodDesc);

    if (actual <= times) {
      return this;
    } else {
      // TODO Stringbuilder...... on both of these (above).
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + TO_BE_CALLED
              + TypeStringUtils.buildArgsString(invocation)
              + "at most " + this.readableTimes(times) + BUT_IT_WAS_CALLED
              + this.readableTimes(actual));
    }
  }

  int countExceptionsThrown(final Invocation invocation,
                            final String methodName,
                            final String methodDesc,
                            final Predicate<? super Invocation> filterPredicate) {
    return this.getEngine()
        .getRecorder()
        .getInvocationList(invocation.getReceiver().getClass(),
                           methodName,
                           methodDesc)
      .stream()
      .filter( e -> this.getEngine()
                      .getASMMatcherEngine()
                      .argsMatch(e.getArgs(), invocation.getArgs()) )
      .filter(filterPredicate)
      .collect(Collectors.toList())
      .size();
  }

  @Override
  public MoxyVerifier neverThrew(final Class<? extends Throwable> throwableClass) {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();

    final int actual = (this.countExceptionsThrown(invocation,
                                        invocation.getMethodName(),
                                        invocation.getMethodDesc(),
        e -> e.getThrew() != null && e.getThrew().getClass().equals(throwableClass))
    );

    if (actual > 0) {
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + " "
              + TypeStringUtils.buildArgsString(invocation)
              + "never to throw exception class " + throwableClass.getName()
              + ", but it was thrown " + this.readableTimes(actual));

    } else {
      return this;
    }
  }

  @Override
  public MoxyVerifier neverThrew(final Throwable throwable) {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();

    final int actual = (this.countExceptionsThrown(invocation,
                                        invocation.getMethodName(),
                                        invocation.getMethodDesc(),
        e -> e.getThrew() != null && e.getThrew().equals(throwable))
    );

    if (actual > 0) {
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + " "
              + TypeStringUtils.buildArgsString(invocation)
              + "never to throw exception " + throwable
              + ", but it was thrown " + this.readableTimes(actual));

    } else {
      return this;
    }
  }

  @Override
  public MoxyVerifier neverThrewAnyException() {
    final Invocation invocation = this.getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();

    final int actual = (this.countExceptionsThrown(invocation,
                                        invocation.getMethodName(),
                                        invocation.getMethodDesc(),
        e -> e.getThrew() != null)
    );

    if (actual > 0) {
      throw new AssertionFailedError(
          EXPECTED_MOCK + methodName + TypeStringUtils.ellipsisDesc(methodDesc) + " "
              + TypeStringUtils.buildArgsString(invocation)
              + "never to throw any exception, but exceptions were thrown " + this.readableTimes(actual));
    } else {
      return this;
    }
  }
}
