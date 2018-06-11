package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleInterface;

public class TestMoxyPartialMocks {
  @Test
  public void testMoxyMockWithSpecificMethodsWorks() throws Exception {
    final MethodWithArgAndReturn control = new MethodWithArgAndReturn();

    final Class<? extends MethodWithArgAndReturn> mockClass =
        Moxy.getMoxyEngine().getMockClass(MethodWithArgAndReturn.class,
            Collections.singleton(MethodWithArgAndReturn.class.getMethod(
                "hasTwoArgs", String.class, int.class)));

    final MethodWithArgAndReturn mock = mockClass
        .getConstructor(MoxyEngine.class).newInstance(Moxy.getMoxyEngine());

    assertThat(control.hasTwoArgs("test", 1)).isEqualTo("test1");
    assertThat(mock.hasTwoArgs("test", 1)).isNull();

    assertThat(control.sayHelloTo("Steve"))
        .isEqualTo(mock.sayHelloTo("Steve"));
  }

  @Test
  public void testMoxyMockWithSpecificMethodsStubNonMockFailsFast() throws Exception {
    final MethodWithArgAndReturn control = new MethodWithArgAndReturn();

    final Class<? extends MethodWithArgAndReturn> mockClass =
        Moxy.getMoxyEngine().getMockClass(MethodWithArgAndReturn.class,
            Collections.singleton(MethodWithArgAndReturn.class.getMethod(
                "hasTwoArgs", String.class, int.class)));

    final MethodWithArgAndReturn mock = mockClass
        .getConstructor(MoxyEngine.class).newInstance(Moxy.getMoxyEngine());

    assertThat(control.hasTwoArgs("test", 1)).isEqualTo("test1");
    assertThat(mock.hasTwoArgs("test", 1)).isNull();

    assertThatThrownBy(() ->
        Moxy.when(() -> mock.sayHelloTo("Steve")).thenReturn("failed")
    )
        .isInstanceOf(InvalidMockInvocationException.class)
        .hasMessage("No mock invocation found");

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Hello, Steve");
  }

  @Test
  public void testMoxyMockWithSpecificMethodsForcesAbstractsInInterfaces() throws Exception {
    final Class<? extends SimpleInterface> mockClass =
        Moxy.getMoxyEngine().getMockClass(SimpleInterface.class,
              Collections.singleton(SimpleInterface.class.getMethod("returnHello")));

    final SimpleInterface mock = mockClass
        .getConstructor(MoxyEngine.class).newInstance(Moxy.getMoxyEngine());

    assertThat(mock.returnHello()).isNull();
    assertThat(mock.returnGoodbye()).isNull();

    Moxy.when(() -> mock.returnHello()).thenReturn("Hello!");
    Moxy.when(() -> mock.returnGoodbye()).thenReturn("Goodbye!");

    assertThat(mock.returnHello()).isEqualTo("Hello!");
    assertThat(mock.returnGoodbye()).isEqualTo("Goodbye!");
  }

  @Test
  public void testMoxyMockWithSpecificMethodsForcesAbstractsInClasses() throws Exception {
    final Class<? extends SimpleAbstractClass> mockClass =
        Moxy.getMoxyEngine().getMockClass(SimpleAbstractClass.class,
              Collections.singleton(SimpleAbstractClass.class.getMethod("concreteMethod")));

    final SimpleAbstractClass mock = mockClass
        .getConstructor(MoxyEngine.class).newInstance(Moxy.getMoxyEngine());

    assertThat(mock.returnHello()).isNull();
    assertThat(mock.concreteMethod()).isNull();

    Moxy.when(() -> mock.returnHello()).thenReturn("Hello!");
    Moxy.when(() -> mock.concreteMethod()).thenReturn("Concrete!");

    assertThat(mock.returnHello()).isEqualTo("Hello!");
    assertThat(mock.concreteMethod()).isEqualTo("Concrete!");
  }
}
