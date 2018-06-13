package com.roscopeco.moxy.impl.asm;

import java.util.List;
import java.util.stream.Collectors;

import org.opentest4j.AssertionFailedError;

class VerifierHelpers {
  static boolean invocationsMatch(final ASMMoxyMatcherEngine engine,
                          final Invocation actual,
                          final Invocation stored) {
    return
        stored.getReceiver() == actual.getReceiver() &&     // identity is intentional!
        stored.getMethodName().equals(actual.getMethodName()) &&
        stored.getMethodDesc().equals(actual.getMethodDesc()) &&
        engine.argsMatch(stored.getArgs(), actual.getArgs());
  }

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
          .filter(e -> invocationsMatch(engine, invocationToMatch, e))
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
        .map(Invocation::toString)
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

  /*
   * Tests for an ordered match of the monitored invocations in the list
   * of actual invocations.
   *
   * if exclusiveMode is true, the invocations must immediately follow
   * one-another in order to match.
   */
  static void testOrderedMatch(final ASMMoxyMatcherEngine engine,
                               final List<Invocation> actualInvocations,
                               final List<Invocation> monitoredInvocations,
                               final boolean exclusiveMode) {
    int currentMatchPtr = 0;
    boolean inMatch = false;

    for (final Invocation actual : actualInvocations) {
      final Invocation monitored = monitoredInvocations.get(currentMatchPtr);

      if (!inMatch) {
        // could this be the start of our match?
        if (invocationsMatch(engine, actual, monitored)) {
          inMatch = true;
          currentMatchPtr++;
        }
      } else {
        // currently in a match
        if (exclusiveMode) {
          // exclusive mode, so reset matchPtr if next invocation isn't
          // also a match (i.e. our wanted invocations don't exactly follow
          // one-another).
          if (!invocationsMatch(engine, actual, monitored)) {
            currentMatchPtr = 0;
            inMatch = false;
          } else {
            // They do match, so move on to next
            currentMatchPtr++;
          }
        } else {
          // not exclusive, so if this isn't a match, just move on.
          if (invocationsMatch(engine, actual, monitored)) {
            currentMatchPtr++;
          }
        }
      }

      // If we've matched all our monitored invocations, test passed.
      if (currentMatchPtr == monitoredInvocations.size()) {
        return;
      }
    }

    // If we got to here, we didn't find a match. Test failed.
    throw new AssertionFailedError(
        makeOrderMismatchMessage(monitoredInvocations, exclusiveMode));
  }

  static String makeOrderMismatchMessage(final List<Invocation> expected,
                                         final boolean exclusiveMode) {
    final StringBuilder sb =  new StringBuilder()
        .append("Expected invocations:")
        .append(StringConsts.EOL);

    expected.forEach(i -> {
      sb.append(StringConsts.TAB);
      sb.append(i.toString());
      sb.append(StringConsts.EOL);
    });

    if (exclusiveMode) {
      sb.append("exclusively ");
    }

    sb.append("in that order, but they were not found");

    return sb.toString();
  }
}