package com.roscopeco.moxy.matchers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestPossibleMatcherUsageError {
    public static final String MARKER = "MARKER";
    private static final Exception CAUSE = new Exception("CAUSE");

    @Test
    void testStringThrowableConstructor() {
        final PossibleMatcherUsageError ex = new PossibleMatcherUsageError(MARKER, CAUSE);

        assertThat(ex.getMessage()).isEqualTo(MARKER);
        assertThat(ex.getCause()).isEqualTo(CAUSE);
    }
}
