package com.roscopeco.moxy.junit5;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.Moxy;

@ExtendWith(MoxyResetBeforeEach.class)
public class TestMoxyResetBeforeEach {

  private static boolean anyFailed = false;
  private static final Object mock = Moxy.mock(Object.class);

  @BeforeAll
  public static void initialize() {
    anyFailed = false;
  }

  @Test
  public void testMoxyResetBeforeEach01() {
    try {
      Moxy.assertMock(() -> mock.toString()).wasCalled();
      anyFailed = true;
    } catch (final AssertionFailedError e) {
      assertThat(e.getMessage()).isEqualTo("Expected mock toString() to be called at least once but it wasn't called at all");
      // test passes
    }

    mock.toString();
  }

  @Test
  public void testMoxyResetBeforeEach02() {
    try {
      Moxy.assertMock(() -> mock.toString()).wasCalled();
      anyFailed = true;
    } catch (final Throwable e) {
      assertThat(e.getMessage()).isEqualTo("Expected mock toString() to be called at least once but it wasn't called at all");
      // test passes
    }

    mock.toString();
  }

  @AfterAll
  public static void theRealTest() {
    assertThat(anyFailed).isFalse();
  }
}