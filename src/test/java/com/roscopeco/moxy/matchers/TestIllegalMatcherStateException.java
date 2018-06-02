package com.roscopeco.moxy.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestIllegalMatcherStateException {
  public static final String MARKER = "MARKER";
  public static final Exception CAUSE = new Exception("CAUSE");

  @Test
  public void TestStringConstructor() {
    final IllegalMatcherStateException ex = new IllegalMatcherStateException(MARKER);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isNull();
  }
}
