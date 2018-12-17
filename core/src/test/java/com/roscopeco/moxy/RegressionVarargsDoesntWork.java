package com.roscopeco.moxy;

import org.junit.jupiter.api.Test;

import static com.roscopeco.moxy.Moxy.mock;
import static com.roscopeco.moxy.Moxy.when;
import static org.assertj.core.api.Assertions.assertThat;

class RegressionVarargsDoesntWork {
  @Test
  void testWithVarargs() {
    InterfaceWithVarargsMethod va = mock(InterfaceWithVarargsMethod.class);

    when(() -> va.varargsMethod("String")).thenReturn(true);

    assertThat(va.varargsMethod("String")).isTrue();
  }

  interface InterfaceWithVarargsMethod {
    boolean varargsMethod(Object... args);
  }
}
