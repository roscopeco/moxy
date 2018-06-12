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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.MoxyVerifier;

class ASMMoxyVerifier extends AbstractASMMoxyVerifier implements MoxyVerifier {
  public ASMMoxyVerifier(final ASMMoxyEngine engine, final List<Invocation> theInvocations) {
    super(engine, theInvocations);
  }

  @Override
  public MoxyVerifier wasCalled() {
    final Invocation invocation = this.getLastMonitoredInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();

    if (this.getEngine()
            .getRecorder()
            .getInvocationList(invocation.getReceiver().getClass(),
                               methodName,
                               methodDesc)
        .stream()
        .anyMatch(e -> this.getEngine()
                             .getMatcherEngine()
                             .argsMatch(e.getArgs(), invocation.getArgs())
    )) {
      return this;
    } else {
      throw new AssertionFailedError(
          new StringBuilder()
            .append(StringConsts.EXPECTED_MOCK)
            .append(invocation.toString())
            .append(StringConsts.TO_BE_CALLED)
            .append(StringConsts.AT_LEAST_ONCE_BUT_WASNT_AT_ALL)
                .toString());
    }
  }

  int getCallCount(final Invocation invocation) {
    return VerifierHelpers.getCallCount(
        this.getEngine().getMatcherEngine(),
        invocation,
        this.getEngine()
          .getRecorder()
          .getInvocationList(invocation.getReceiver().getClass(),
                             invocation.getMethodName(),
                             invocation.getMethodDesc()));
  }

  @Override
  public MoxyVerifier wasCalled(final int times) {
    final Invocation invocation = this.getLastMonitoredInvocation();
    final int actual = this.getCallCount(invocation);

    if (actual == times) {
      return this;
    } else {
      throw new AssertionFailedError(
          VerifierHelpers.makeExpectedCountMismatchMessage(invocation,
                                                           times,
                                                           actual,
                                                           StringConsts.EXACTLY));
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
    final Invocation invocation = this.getLastMonitoredInvocation();
    final int actual = this.getCallCount(invocation);

    if (actual >= times) {
      return this;
    } else {
      throw new AssertionFailedError(
          VerifierHelpers.makeExpectedCountMismatchMessage(invocation,
                                                           times,
                                                           actual,
                                                           StringConsts.AT_LEAST));
    }
  }

  @Override
  public MoxyVerifier wasCalledAtMost(final int times) {
    final Invocation invocation = this.getLastMonitoredInvocation();
    final int actual = this.getCallCount(invocation);

    if (actual <= times) {
      return this;
    } else {
      throw new AssertionFailedError(
          VerifierHelpers.makeExpectedCountMismatchMessage(invocation,
              times,
              actual,
              StringConsts.AT_MOST));
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
                      .getMatcherEngine()
                      .argsMatch(e.getArgs(), invocation.getArgs()) )
      .filter(filterPredicate)
      .collect(Collectors.toList())
      .size();
  }

  @Override
  public MoxyVerifier didntThrow(final Class<? extends Throwable> throwableClass) {
    final Invocation invocation = this.getLastMonitoredInvocation();

    final int actual = (this.countExceptionsThrown(invocation,
                                        invocation.getMethodName(),
                                        invocation.getMethodDesc(),
        e -> e.getThrew() != null && e.getThrew().getClass().equals(throwableClass))
    );

    if (actual > 0) {
      throw new AssertionFailedError(
          new StringBuilder()
            .append(StringConsts.EXPECTED_MOCK)
            .append(invocation.toString())
            .append(StringConsts.SPACE)
            .append(StringConsts.NEVER_TO_THROW_EXCEPTION)
            .append("class ")
            .append(throwableClass.getName())
            .append(StringConsts.BUT_IT_WAS_THROWN)
            .append(TypeStringUtils.readableTimes(actual))
                .toString());
    } else {
      return this;
    }
  }

  @Override
  public MoxyVerifier didntThrow(final Throwable throwable) {
    final Invocation invocation = this.getLastMonitoredInvocation();

    final int actual = (this.countExceptionsThrown(invocation,
                                        invocation.getMethodName(),
                                        invocation.getMethodDesc(),
        e -> e.getThrew() != null && e.getThrew().equals(throwable))
    );

    if (actual > 0) {
      throw new AssertionFailedError(
          new StringBuilder()
            .append(StringConsts.EXPECTED_MOCK)
            .append(invocation.toString())
            .append(StringConsts.SPACE)
            .append(StringConsts.NEVER_TO_THROW_EXCEPTION)
            .append(throwable)
            .append(StringConsts.BUT_IT_WAS_THROWN)
            .append(TypeStringUtils.readableTimes(actual))
                .toString());
    } else {
      return this;
    }
  }

  @Override
  public MoxyVerifier didntThrowAnyException() {
    final Invocation invocation = this.getLastMonitoredInvocation();

    final int actual = (this.countExceptionsThrown(invocation,
                                        invocation.getMethodName(),
                                        invocation.getMethodDesc(),
        e -> e.getThrew() != null)
    );

    if (actual > 0) {
      throw new AssertionFailedError(
          new StringBuilder()
            .append(StringConsts.EXPECTED_MOCK)
            .append(invocation.toString())
            .append(StringConsts.SPACE)
            .append(StringConsts.NEVER_THROW_ANY_BUT_EXCEPTIONS_THROWN)
            .append(TypeStringUtils.readableTimes(actual))
                .toString());
    } else {
      return this;
    }
  }
}
