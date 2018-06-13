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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.InvalidStubbingException;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleInterface;

public class TestMoxyCallRealMethod {
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testMoxyMockThenCallRealMethod() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo("Bill")).thenCallRealMethod();

    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");
    assertThat(mock.sayHelloTo("Steve")).isEqualTo(null);
  }

  @Test
  public void testMoxyMockThenCallRealMethodVerifyCalledWorksAsNormal() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo("Bill")).thenCallRealMethod();

    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");

    Moxy.assertMock(() -> mock.sayHelloTo("Bill")).wasCalledOnce();
    Moxy.assertMock(() -> mock.sayHelloTo("Steve")).wasNotCalled();
    Moxy.assertMock(() -> mock.sayHelloTo("Bill")).didntThrowAnyException();
  }

  @Test
  public void testMoxyMockThenCallRealMethodVerifyNeverThrewWorksAsNormal() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo("Hamburglar")).thenCallRealMethod();

    assertThatThrownBy(() ->
        mock.sayHelloTo("Hamburglar")
    )
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Hamburglar detected!");

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.sayHelloTo("Hamburglar")).didntThrowAnyException()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock sayHelloTo(\"Hamburglar\") never to throw any exception, but exceptions were thrown once");
  }

  @Test
  public void testMoxyMockThenCallRealMethodWorksWithPrimitiveArgs() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    assertThat(mock.hasArgs("test", (byte)42, 'a', (short)42, 42, 42L, 42.0f, 42.0D, true))
        .isEqualTo(0);

    Moxy.when(() -> mock.hasArgs("test", (byte)42, 'a', (short)42, 42, 42L, 42.0f, 42.0D, true))
        .thenCallRealMethod();

    assertThat(mock.hasArgs("test", (byte)42, 'a', (short)42, 42, 42L, 42.0f, 42.0D, true))
        .isEqualTo(42);
  }

  @Test
  public void testMoxyMockThenCallRealMethodWorksWithPrimitiveReturns() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    assertThat(mock.returnByte()).isEqualTo((byte)0);
    assertThat(mock.returnChar()).isEqualTo((char)0);
    assertThat(mock.returnShort()).isEqualTo((short)0);
    assertThat(mock.returnInt()).isEqualTo(0);
    assertThat(mock.returnLong()).isEqualTo(0L);
    assertThat(mock.returnFloat()).isEqualTo(0.0f);
    assertThat(mock.returnDouble()).isEqualTo(0.0d);
    assertThat(mock.returnBoolean()).isEqualTo(false);

    Moxy.when(() -> mock.returnByte()).thenCallRealMethod();
    Moxy.when(() -> mock.returnChar()).thenCallRealMethod();
    Moxy.when(() -> mock.returnShort()).thenCallRealMethod();
    Moxy.when(() -> mock.returnInt()).thenCallRealMethod();
    Moxy.when(() -> mock.returnLong()).thenCallRealMethod();
    Moxy.when(() -> mock.returnFloat()).thenCallRealMethod();
    Moxy.when(() -> mock.returnDouble()).thenCallRealMethod();
    Moxy.when(() -> mock.returnBoolean()).thenCallRealMethod();

    assertThat(mock.returnByte()).isEqualTo((byte)10);
    assertThat(mock.returnChar()).isEqualTo('a');
    assertThat(mock.returnShort()).isEqualTo((short)10);
    assertThat(mock.returnInt()).isEqualTo(10);
    assertThat(mock.returnLong()).isEqualTo(10L);
    assertThat(mock.returnFloat()).isEqualTo(10.0f);
    assertThat(mock.returnDouble()).isEqualTo(10.0d);
    assertThat(mock.returnBoolean()).isEqualTo(true);
  }

  @Test
  public void testMoxyMockThenCallRealMethodWithAbstractClassFailsFast() {
    final SimpleAbstractClass mock = Moxy.mock(SimpleAbstractClass.class);

    Moxy.when(() -> mock.returnHello()).thenCallRealMethod();

    assertThatThrownBy(() ->
        mock.returnHello()
    )
        .isInstanceOf(InvalidStubbingException.class)
        .hasMessage("Cannot call real method 'java.lang.String returnHello()' (it is abstract)");
  }

  @Test
  public void testMoxyMockThenCallRealMethodWithInterfaceFailsFast() {
    final SimpleInterface mock = Moxy.mock(SimpleInterface.class);

    Moxy.when(() -> mock.returnHello()).thenCallRealMethod();

    assertThatThrownBy(() ->
        mock.returnHello()
    )
        .isInstanceOf(InvalidStubbingException.class)
        .hasMessage("Cannot call real method 'java.lang.String returnHello()' (it is abstract)");
  }
}
