package com.roscopeco.moxy.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestInvalidMockInvocationException {
  public static final String MARKER = "MARKER";
  public static final Exception CAUSE = new Exception("CAUSE");

  @Test
  public void testNullConstructor() {
    final InvalidMockInvocationException ex = new InvalidMockInvocationException();

    assertThat(ex.getMessage()).isNull();
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringConstructor() {
    final InvalidMockInvocationException ex = new InvalidMockInvocationException(MARKER);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringThrowableConstructor() {
    final InvalidMockInvocationException ex = new InvalidMockInvocationException(MARKER, CAUSE);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }

  @Test
  public void TestThrowableConstructor() {
    final InvalidMockInvocationException ex = new InvalidMockInvocationException(CAUSE);

    assertThat(ex.getMessage()).isEqualTo("java.lang.Exception: CAUSE");
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }


}
