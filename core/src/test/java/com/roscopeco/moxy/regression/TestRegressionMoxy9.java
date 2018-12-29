package com.roscopeco.moxy.regression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;

class TestRegressionMoxy9 {
  private static class Super {
    String notOverriden() {
      return "ORIGINAL";
    }

    public String overridden() {
      return "OVERRIDE ME";
    }
  }

  private static class Sub extends Super {
    @Override
    public String overridden() {
      return "OVERRIDEN";
    }
  }


  @Test
  void moxyBug() {
    Sub sub = Moxy.mock(Sub.class);

    assertThat(sub.overridden()).isNull();
    assertThat(sub.notOverriden()).isNull();     /* failure here */
  }
}