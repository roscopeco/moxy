package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

import com.roscopeco.moxy.api.MoxyMultiVerifier;

class ASMMoxyMultiVerifier extends AbstractASMMoxyVerifier implements MoxyMultiVerifier {
  private boolean allCalledChecked = false;

  public ASMMoxyMultiVerifier(final ASMMoxyEngine engine, final List<Invocation> invocations) {
    super(engine, Collections.unmodifiableList(invocations));
  }

  int getCallCount(final Invocation invocation, final List<Invocation> actualInvocations) {
    return VerifierHelpers.getCallCount(this.getEngine().getMatcherEngine(), invocation, actualInvocations);
  }

  /*
   * Handles all the wereXXXXCalled methods.
   */
  MoxyMultiVerifier wereCalled(final int expectedTimes, final StringConsts comparison,
      final Function<Integer, Boolean> passCondition) {

    // This flag is used later if ordering is checked, to provide a sensible failure message...
    this.allCalledChecked = true;

    final List<Invocation> actualInvocations = new LinkedList<>(this.getRecorder().getInvocationList());
    final List<AssertionFailedError> errors = new ArrayList<>(actualInvocations.size());

    this.getMonitoredInvocations().forEach(monitored -> {
      final int callCount = this.getCallCount(monitored, actualInvocations);

      if (!passCondition.apply(callCount)) {
        errors.add(new AssertionFailedError(
            VerifierHelpers.makeExpectedCountMismatchMessage(monitored, expectedTimes, callCount, comparison)));
      }
    });

    if (errors.size() == 1) {
      throw errors.get(0);
    } else if (errors.size() > 1) {
      throw new MultipleFailuresError("There were unexpected invocations", errors);
    } else {
      return this;
    }
  }

  @Override
  public void wereNotCalled() {
    this.wereAllCalledExactly(0);
  }

  @Override
  public MoxyMultiVerifier wereAllCalled() {
    return this.wereAllCalledAtLeast(1);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledExactly(final int expectedTimes) {
    return this.wereCalled(expectedTimes, StringConsts.EXACTLY, callCount -> callCount == expectedTimes);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledOnce() {
    return this.wereAllCalledExactly(1);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledTwice() {
    return this.wereAllCalledExactly(2);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledAtLeast(final int expectedTimes) {
    return this.wereCalled(expectedTimes, StringConsts.AT_LEAST, callCount -> callCount >= expectedTimes);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledAtLeastTwice() {
    return this.wereAllCalledAtLeast(2);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledAtMost(final int expectedTimes) {
    return this.wereCalled(expectedTimes, StringConsts.AT_MOST, callCount -> callCount <= expectedTimes);
  }

  @Override
  public MoxyMultiVerifier wereAllCalledAtMostTwice() {
    return this.wereAllCalledAtMost(2);
  }

  @Override
  public void inThatOrder() {
    VerifierHelpers.testOrderedMatch(this.getEngine().getMatcherEngine(),
                                     this.getEngine().getRecorder().getInvocationList(),
                                     this.getMonitoredInvocations(),
                                     false,
                                     this.allCalledChecked);
  }

  @Override
  public void exclusivelyInThatOrder() {
    VerifierHelpers.testOrderedMatch(this.getEngine().getMatcherEngine(),
                                     this.getEngine().getRecorder().getInvocationList(),
                                     this.getMonitoredInvocations(),
                                     true,
                                     this.allCalledChecked);
  }

  @Override
  public void inAnyOrder() {
    // Does nothing, as this is the default.
  }
}
