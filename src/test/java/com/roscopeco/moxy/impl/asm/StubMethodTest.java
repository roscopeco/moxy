package com.roscopeco.moxy.impl.asm;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class StubMethodTest {
  @Test
  public void testEqualsHashcode() {
    EqualsVerifier.forClass(StubMethod.class)
        .verify();
  }

}
