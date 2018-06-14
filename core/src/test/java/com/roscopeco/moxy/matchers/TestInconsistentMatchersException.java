package com.roscopeco.moxy.matchers;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.junit.jupiter.api.Test;

public class TestInconsistentMatchersException {
  static class FakeMatcher<T> implements MoxyMatcher<T> {
    @Override
    public boolean matches(final T arg) {
      return false;
    }

    @Override
    public String toString() {
      return FAKEMATCHER;
    }
  }

  private static final String FAKEMATCHER = "FAKEMATCHER";
  public static final int EXPECTED = 2;
  public static final Deque<MoxyMatcher<?>> STACK = new ArrayDeque<>(
      Arrays.asList(new FakeMatcher<Object>()));

  @Test
  public void TestIntDequeConstructor() {
    final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);

    assertThat(ex.getMessage()).isEqualTo(
        "Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
                 + "This limitation will (hopefully) be lifted in the future");

    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void testGetExpectedSize() {
    final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);

    assertThat(ex.getExpectedSize()).isEqualTo(2);
  }

  @Test
  public void testGetActualSize() {
    final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);

    assertThat(ex.getActualSize()).isEqualTo(1);
  }

  @Test void testGetStack() {
    final InconsistentMatchersException ex = new InconsistentMatchersException(EXPECTED, STACK);
    final Deque<MoxyMatcher<?>> stack = ex.getStack();

    assertThat(stack)
        .isNotNull()
        .isNotSameAs(STACK)           // Make sure it's a copy
        .hasSameSizeAs(STACK)
        .hasSameElementsAs(STACK);
  }
}