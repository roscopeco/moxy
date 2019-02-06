package com.roscopeco.moxy.regression;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;

class TestRegressionMoxy13 {
  public interface Super {
    void superMethod();
  }

  public interface Sub extends Super {
    void subMethod();
  }

  @Test
  void testMoxyBug() {
    final Sub sub = Moxy.mock(Sub.class, System.out);

    sub.subMethod();        // Works fine
    sub.superMethod();      // AbstractMethodError is thrown; Output shows superMethod was *not* generated
  }
}