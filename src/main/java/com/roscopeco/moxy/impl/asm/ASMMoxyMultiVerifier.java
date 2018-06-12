package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

import com.roscopeco.moxy.api.MoxyMultiVerifier;

public class ASMMoxyMultiVerifier extends AbstractASMMoxyVerifier implements MoxyMultiVerifier {
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
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public MoxyMultiVerifier exclusivelyInOrder() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void wereNotCalled() {
    final List<Invocation> actualInvocations = new LinkedList<>(this.getRecorder().getInvocationList());
    final List<AssertionFailedError> errors = new ArrayList<>(actualInvocations.size());

    this.getMonitoredInvocations().forEach(actual -> {
      final int callCount = this.getCallCount(actual, actualInvocations);

      if (callCount > 0) {
        errors.add(new AssertionFailedError(
            VerifierHelpers.makeExpectedCountMismatchMessage(actual, 0, callCount, StringConsts.EXACTLY)));
      }
    });

    if (errors.size() == 1) {
      throw errors.get(0);
    } else if (errors.size() > 1) {
      throw new MultipleFailuresError("There were unexpected invocations", errors);
    }
  }

  @Override
  public void wereAllCalled() {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
