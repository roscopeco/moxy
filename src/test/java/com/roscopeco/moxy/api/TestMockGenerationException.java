package com.roscopeco.moxy.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestMockGenerationException {
  public static final String MARKER = "MARKER";
  public static final Exception CAUSE = new Exception("CAUSE");

  @Test
  public void testNullConstructor() {
    final MockGenerationException ex = new MockGenerationException();

    assertThat(ex.getMessage()).isNull();
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringConstructor() {
    final MockGenerationException ex = new MockGenerationException(MARKER);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringThrowableConstructor() {
    final MockGenerationException ex = new MockGenerationException(MARKER, CAUSE);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }

  @Test
  public void TestThrowableConstructor() {
    final MockGenerationException ex = new MockGenerationException(CAUSE);

    assertThat(ex.getMessage()).isEqualTo("java.lang.Exception: CAUSE");
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }


}
