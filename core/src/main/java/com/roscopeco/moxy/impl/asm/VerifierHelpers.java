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

import org.opentest4j.AssertionFailedError;

import java.util.List;
import java.util.stream.Collectors;

class VerifierHelpers {
    private static boolean invocationsMatch(final ASMMoxyMatcherEngine engine,
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
    static long getCallCount(final ASMMoxyMatcherEngine engine,
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
                    .count();
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
                                                   final long expectedTimes,
                                                   final long actualTimes,
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
                                 final boolean exclusiveMode,
                                 final boolean allCalledChecked) {
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
                makeOrderMismatchMessage(monitoredInvocations, exclusiveMode, allCalledChecked));
    }

    private static String makeOrderMismatchMessage(final List<Invocation> expected,
                                                   final boolean exclusiveMode,
                                                   final boolean allCalledChecked) {
        final StringBuilder sb = new StringBuilder()
                .append(StringConsts.EXPECTED_INVOCATIONS)
                .append(StringConsts.EOL);

        expected.forEach(i -> {
            sb.append(StringConsts.TAB);
            sb.append(i.toString());
            sb.append(StringConsts.EOL);
        });

        if (exclusiveMode) {
            sb.append(StringConsts.EXCLUSIVELY);
        }

        sb.append(StringConsts.IN_ORDER_BUT_WERE);

        if (!allCalledChecked) {
            sb.append(StringConsts.NOT_INVOKED_OR);
        }

        sb.append(StringConsts.OUT_OF_ORDER);

        return sb.toString();
    }
}
