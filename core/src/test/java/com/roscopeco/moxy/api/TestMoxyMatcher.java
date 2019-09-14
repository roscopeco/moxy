package com.roscopeco.moxy.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;

import org.junit.jupiter.api.Test;

class TestMoxyMatcher {
    static class FakeMatcher implements MoxyMatcher<Object> {
        @Override
        public boolean matches(final Object arg) {
            return false;
        }
    }

    @Test
    void testAddToStack() {
        final var stack = new ArrayDeque<MoxyMatcher<?>>();
        final var fm = new FakeMatcher();

        fm.addToStack(stack);

        assertThat(stack).containsExactly(fm);
    }
}
