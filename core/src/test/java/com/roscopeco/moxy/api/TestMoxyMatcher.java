package com.roscopeco.moxy.api;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;

import static org.assertj.core.api.Assertions.assertThat;

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
