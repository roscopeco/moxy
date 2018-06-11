package com.roscopeco.moxy.impl.asm;

import java.util.List;
import java.util.stream.Collectors;

import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.MoxyMultiVerifier;

public class ASMMoxyMultiVerifier extends AbstractASMMoxyVerifier implements MoxyMultiVerifier {
  private static final String EXPECTED_MOCK = "Expected mock ";
  private static final String EXPECTED_MOCKS = "Expected mocks ";
  private static final String TO_NOT_BE_CALLED_SINGLE = " to not be called, but it was.";
  private static final String TO_NOT_BE_CALLED_MULTI = " to not be called, but they were.";

  public ASMMoxyMultiVerifier(final ASMMoxyEngine engine, final List<Invocation> invocations) {
    super(engine, invocations);
  }

  private String shortMethodSignatures(final List<Invocation> invocations) {
    final StringBuilder sb = new StringBuilder();
    if (invocations.size() > 1) {
      sb.append("[");
    }

    sb.append(invocations.stream().map(i ->
          i.getMethodName() + TypeStringUtils.shortDescriptorSignature(i.getMethodDesc()))
              .collect(Collectors.joining(", ")));

    if (invocations.size() > 1) {
      sb.append("]");
    }

    return sb.toString();
  }

  @Override
  public MoxyMultiVerifier wereNotCalled() {
    final List<Invocation> actualInvocations = this.getRecorder().getInvocationList();

    final List<Invocation> matches = actualInvocations.stream()
          .filter(actual ->
              this.getInvocations().stream().anyMatch(invocation ->
                  invocation.getMethodName().equals(actual.getMethodName())
                      && invocation.getMethodDesc().equals(actual.getMethodDesc())
                  )
              && this.getEngine().getMatcherEngine().anyArgsMatch(actual.getArgs(),
                                                                  this.getInvocations()))
          .collect(Collectors.toList());

    if (matches.size() == 1) {
      throw new AssertionFailedError(
          new StringBuilder()
            .append(EXPECTED_MOCK)
            .append(this.shortMethodSignatures(matches))
            .append(TO_NOT_BE_CALLED_SINGLE)
                .toString());
    } else if (matches.size() > 1) {
      throw new AssertionFailedError(
          new StringBuilder()
            .append(EXPECTED_MOCKS)
            .append(this.shortMethodSignatures(matches))
            .append(TO_NOT_BE_CALLED_MULTI)
                .toString());
    } else {
      return this;
    }
  }
}
