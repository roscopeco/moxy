package com.roscopeco.moxy.impl.asm;

import java.util.List;
import java.util.stream.Collectors;

class VerifierHelpers {
  /*
   * Return number of times invocationToMatch matches in invocations.
   */
  static int getCallCount(final ASMMoxyMatcherEngine engine,
                          final Invocation invocationToMatch,
                          final List<Invocation> invocations) {
    if (engine == null) {
      throw new IllegalArgumentException("Cannot match arguments with null engine");
    } else if (invocationToMatch == null) {
      throw new IllegalArgumentException("Cannot match arguments with null invocation");
    } else if (invocations == null) {
      throw new IllegalArgumentException("Cannot match arguments with null invocation list");
    } else {
      return invocations
          .stream()
          .filter(e ->
            e.getReceiver() == invocationToMatch.getReceiver() &&     // identity is intentional!
            e.getMethodName().equals(invocationToMatch.getMethodName()) &&
            e.getMethodDesc().equals(invocationToMatch.getMethodDesc()) &&
            engine.argsMatch(e.getArgs(), invocationToMatch.getArgs())
          )
          .collect(Collectors.toList())
          .size();
    }
  }

  private VerifierHelpers() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.VerifierHelpers is not designed for instantiation");
  }

  /*
   * Return a human-readble list of invocations.
   */
  static String inspectInvocations(final List<Invocation> invocations) {
    final StringBuilder sb = new StringBuilder();
    if (invocations.size() > 1) {
      sb.append(StringConsts.LSQPAREN);
    }

    sb.append(invocations.stream()
        .map(i -> i.toString())
        .collect(Collectors.joining(StringConsts.COMMA_SPACE.value())));

    if (invocations.size() > 1) {
      sb.append(StringConsts.RSQPAREN);
    }

    return sb.toString();
  }

  static String makeExpectedCountMismatchMessage(final Invocation invocation,
                                                 final int expectedTimes,
                                                 final int actualTimes,
                                                 final StringConsts comparison) {
    return new StringBuilder()
    .append(StringConsts.EXPECTED_MOCK)
    .append(invocation.toString())
    .append(StringConsts.TO_BE_CALLED)
    .append(comparison)
    .append(StringConsts.SPACE)
    .append(TypeStringUtils.readableTimes(expectedTimes))
    .append(StringConsts.BUT_IT_WAS_CALLED)
    .append(TypeStringUtils.readableTimes(actualTimes))
        .toString();

  }
}
