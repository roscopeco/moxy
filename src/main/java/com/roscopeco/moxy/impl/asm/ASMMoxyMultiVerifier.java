package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

import com.roscopeco.moxy.api.MoxyMultiVerifier;

public class ASMMoxyMultiVerifier extends AbstractASMMoxyVerifier implements MoxyMultiVerifier {
  private boolean inOrderMode = false;
  private boolean exclusiveMode = false;

  public ASMMoxyMultiVerifier(final ASMMoxyEngine engine, final List<Invocation> invocations) {
    super(engine, invocations);
  }

  int getCallCount(final Invocation invocation, final List<Invocation> actualInvocations) {
    return VerifierHelpers.getCallCount(this.getEngine().getMatcherEngine(),
                                        invocation,
                                        actualInvocations);
  }

  @Override
  public MoxyMultiVerifier inOrder() {
    this.inOrderMode = true;
    return this;
  }

  @Override
  public MoxyMultiVerifier inAnyOrder() {
    this.inOrderMode = false;
    return this;
  }

  @Override
  public MoxyMultiVerifier exclusively() {
    this.exclusiveMode = true;
    return this;
  }

  void wereCalled(final int expectedTimes, final StringConsts comparison, final Function<Integer, Boolean> passCondition) {
    final List<Invocation> actualInvocations = new LinkedList<>(this.getRecorder().getInvocationList());
    final List<AssertionFailedError> errors = new ArrayList<>(actualInvocations.size());

    this.getMonitoredInvocations().forEach(actual -> {
      final int callCount = this.getCallCount(actual, actualInvocations);

      if (!passCondition.apply(callCount)) {
        errors.add(new AssertionFailedError(
            VerifierHelpers.makeExpectedCountMismatchMessage(actual, expectedTimes, callCount, comparison)));
      }
    });

    if (errors.size() == 1) {
      throw errors.get(0);
    } else if (errors.size() > 1) {
      throw new MultipleFailuresError("There were unexpected invocations", errors);
    }
  }

  @Override
  public void wereNotCalled() {
    this.wereAllCalledExactly(0);
  }

  @Override
  public void wereAllCalled() {
    this.wereAllCalledAtLeast(1);
  }

  @Override
  public void wereAllCalledExactly(final int expectedTimes) {
    this.wereCalled(expectedTimes, StringConsts.EXACTLY, callCount -> callCount == expectedTimes);
  }

  @Override
  public void wereAllCalledOnce() {
    this.wereAllCalledExactly(1);
  }

  @Override
  public void wereAllCalledTwice() {
    this.wereAllCalledExactly(2);
  }

  @Override
  public void wereAllCalledAtLeast(final int expectedTimes) {
    this.wereCalled(expectedTimes, StringConsts.AT_LEAST, callCount -> callCount >= expectedTimes);
  }

  @Override
  public void wereAllCalledAtMost(final int expectedTimes) {
    this.wereCalled(expectedTimes, StringConsts.AT_MOST, callCount -> callCount <= expectedTimes);
  }
}
