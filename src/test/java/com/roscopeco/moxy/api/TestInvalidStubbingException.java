package com.roscopeco.moxy.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestInvalidStubbingException {
  public static final String FORMAT = "format: [%s]";
  public static final String MARKER = "MARKER";
  public static final String FORMAT_RESULT = "format: [MARKER]";
  public static final Exception CAUSE = new Exception("CAUSE");

  @Test
  public void testStringConstructor() {
    final InvalidStubbingException ex = new InvalidStubbingException(MARKER);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringStringConstructor() {
    final InvalidStubbingException ex = new InvalidStubbingException(FORMAT, MARKER);

    assertThat(ex.getMessage()).isEqualTo(FORMAT_RESULT);
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringThrowableConstructor() {
    final InvalidStubbingException ex = new InvalidStubbingException(MARKER, CAUSE);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }
}
