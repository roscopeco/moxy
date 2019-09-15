package com.roscopeco.moxy.matchers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestIllegalMatcherStateException {
    public static final String MARKER = "MARKER";
    public static final Exception CAUSE = new Exception("CAUSE");

    @Test
    void testStringConstructor() {
        final IllegalMatcherStateException ex = new IllegalMatcherStateException(MARKER);

        assertThat(ex.getMessage()).isEqualTo(MARKER);
        assertThat(ex.getCause()).isNull();
    }
}
