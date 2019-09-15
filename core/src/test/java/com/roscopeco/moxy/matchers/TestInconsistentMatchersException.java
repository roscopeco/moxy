package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyMatcher;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

import static org.assertj.core.api.Assertions.assertThat;

class TestInconsistentMatchersException {
    static class FakeMatcher<T> implements MoxyMatcher<T> {
        @Override
        public boolean matches(final T arg) {
            return false;
        }

        @Override
        public String toString() {
            return FAKEMATCHER;
        }
    }

    private static final String FAKEMATCHER = "FAKEMATCHER";
    private static final int EXPECTED = 2;
    private static final Deque<MoxyMatcher<?>> STACK = new ArrayDeque<>(
            Collections.singletonList(new FakeMatcher<>()));

    @Test
    void testIntDequeConstructor() {
        final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);

        assertThat(ex.getMessage()).isEqualTo(
                "Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
                        + "This limitation will (hopefully) be lifted in the future.");

        assertThat(ex.getCause()).isNull();
    }

    @Test
    void testGetExpectedSize() {
        final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);

        assertThat(ex.getExpectedSize()).isEqualTo(2);
    }

    @Test
    void testGetActualSize() {
        final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);

        assertThat(ex.getActualSize()).isEqualTo(1);
    }

    @Test
    void testGetStack() {
        final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);
        final Deque<MoxyMatcher<?>> stack = ex.getStack();

        assertThat(stack)
                .isNotNull()
                .isNotSameAs(STACK)           // Make sure it's a copy
                .hasSameSizeAs(STACK)
                .hasSameElementsAs(STACK);
    }
}