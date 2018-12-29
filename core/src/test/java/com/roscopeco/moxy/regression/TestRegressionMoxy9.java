package com.roscopeco.moxy.regression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;

class TestRegressionMoxy9 {
  public static class Super {
    String notOverriden() {
      return "ORIGINAL";
    }

    public String overridden() {
      return "OVERRIDE ME";
    }
  }

  public static class Sub extends Super {
    @Override
    public String overridden() {
      return "OVERRIDEN";
    }
  }


  @Test
  void moxyBug() {
    Sub sub = Moxy.mock(Sub.class);

    assertThat(sub.overridden()).isNull();
    assertThat(sub.notOverriden()).isNull();     /* failure was here */
  }

  @Test
  void testCallRealMethodStillWorks() {
    Sub sub = Moxy.mock(Sub.class);

    Moxy.when(sub::notOverriden).thenCallRealMethod();

    assertThat(sub.overridden()).isNull();
    assertThat(sub.notOverriden()).isEqualTo("ORIGINAL");
  }
}