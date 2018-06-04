package com.roscopeco.moxy.matchers;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestPossibleMatcherUsageError {
  public static final String MARKER = "MARKER";
  public static final Exception CAUSE = new Exception("CAUSE");

  @Test
  public void TestStringThrowableConstructor() {
    final PossibleMatcherUsageError ex = new PossibleMatcherUsageError(MARKER, CAUSE);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }

}
