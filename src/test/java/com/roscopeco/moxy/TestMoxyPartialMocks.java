/*
 * Moxy - Lean-and-mean mocking framework for Java with a fluent API.
 *
 * Copyright 2018 Ross Bamford
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included
 *   in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleInterface;

public class TestMoxyPartialMocks {
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

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
