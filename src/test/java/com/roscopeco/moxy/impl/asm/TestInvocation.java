package com.roscopeco.moxy.impl.asm;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.MoxyException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestInvocation {
  public static final Object RECEIVER = new Object();
  public static final String METHODNAME = "testMethod";
  public static final String METHODDESC = "(Ljava/lang/String;Ljava/lang/String)V";
  public static final List<Object> ARGS = Arrays.asList("one", "two");
  public static final Object RETURNED = new Object();
  public static final Throwable THREW = new Throwable();

  @Test
  public void testConstructor() {
    final Invocation invocation = new Invocation(RECEIVER, METHODNAME, METHODDESC, ARGS);

    assertThat(invocation.getReceiver()).isEqualTo(RECEIVER);
    assertThat(invocation.getMethodName()).isEqualTo(METHODNAME);
    assertThat(invocation.getMethodDesc()).isEqualTo(METHODDESC);
    assertThat(invocation.getArgs()).isEqualTo(ARGS);
  }

  @Test
  public void testConstructorNullReceiver() {
    assertThatThrownBy(() ->
        new Invocation(null, METHODNAME, METHODDESC, ARGS)
    )
        .isInstanceOf(MoxyException.class)
        .hasCauseExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testConstructorNullMethodName() {
    assertThatThrownBy(() ->
        new Invocation(RECEIVER, null, METHODDESC, ARGS)
    )
        .isInstanceOf(MoxyException.class)
        .hasCauseExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testConstructorNullMethodDesc() {
    assertThatThrownBy(() ->
        new Invocation(RECEIVER, METHODNAME, null, ARGS)
    )
        .isInstanceOf(MoxyException.class)
        .hasCauseExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testConstructorNullArgs() {
    final Invocation invocation = new Invocation(RECEIVER, METHODNAME, METHODDESC, null);

    assertThat(invocation.getReceiver()).isEqualTo(RECEIVER);
    assertThat(invocation.getMethodName()).isEqualTo(METHODNAME);
    assertThat(invocation.getMethodDesc()).isEqualTo(METHODDESC);
    assertThat(invocation.getArgs())
        .isNotNull()
        .hasSize(0);
  }

  public void testGetSetReturn() {
    final Invocation invocation = new Invocation(RECEIVER, METHODNAME, METHODDESC, ARGS);

    assertThat(invocation.getReturned()).isNull();

    invocation.setReturned(RETURNED);

    assertThat(invocation.getReturned())
        .isNotNull()
        .isEqualTo(RETURNED);
  }

  public void testGetSetThrew() {
    final Invocation invocation = new Invocation(RECEIVER, METHODNAME, METHODDESC, ARGS);

    assertThat(invocation.getThrew()).isNull();

    invocation.setThrew(THREW);

    assertThat(invocation.getReturned())
        .isNotNull()
        .isEqualTo(THREW);
  }

  @Test
  public void testEqualsHashcode() {
    EqualsVerifier
        .forClass(Invocation.class)
        .withIgnoredFields("returned", "threw")  // ignore mutable fields
        .verify();
  }
}
