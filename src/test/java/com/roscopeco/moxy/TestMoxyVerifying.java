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
import org.opentest4j.MultipleFailuresError;

import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.MoxyMultiVerifier;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleClass;

public class TestMoxyVerifying {
  private static final String EOL = System.lineSeparator();

  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testMoxyAssertMockWithMockReturnsVerifier() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);
    final MoxyVerifier verifier = Moxy.assertMock(() -> mock.returnHello());

    assertThat(verifier)
        .isNotNull()
        .isInstanceOf(MoxyVerifier.class);
  }

  @Test
  public void testMoxyAssertMockWithMockWasCalledFailsIfNotCalled() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThatThrownBy(() -> Moxy.assertMock(() -> mock.returnHello()).wasCalled())
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello() to be called at least once but it wasn't called at all");
  }

  /* We're bootstrapped. Let's start eating our own dog food... */
  @Test
  public void testMoxyAssertMockWithMockWasCalledWorksIfWasCalled() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    mock.returnHello();

    Moxy.assertMock(() -> mock.returnHello()).wasCalled();
  }

  @Test
  public void testMoxyAssertMockWithMockWasCalledWithExactArgumentsWorks() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    mock.hasArgs("One", (byte)2, 'a', (short)20, 0xdeadbeef, 100L, 2468.0f, 4291.0d, true);

    Moxy.assertMock(() -> mock.hasArgs("One", (byte)2, 'a', (short)20, 0xdeadbeef, 100L, 2468.0f, 4291.0d, true))
        .wasCalled();

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.hasArgs("Two", (byte)1, 'b', (short)10, 0x2badf00d, 200L, 3579.0f, 5302.0d, false))
            .wasCalled())
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock hasArgs(\"Two\", (byte)1, 'b', (short)10, 732819469, 200L, 3579.0f, 5302.0d, false) to be called at least once but it wasn't called at all");
  }

  @Test
  public void testMoxyAssertMockWithMockWasCalledExactNumberOfTimesWorks() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    mock.returnHello();
    Moxy.assertMock(() -> mock.returnHello()).wasCalled(1);

    mock.returnHello();
    Moxy.assertMock(() -> mock.returnHello()).wasCalled(2);

    mock.returnHello();
    Moxy.assertMock(() -> mock.returnHello()).wasCalled(3);

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnHello())
            .wasCalled(4))
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello() to be called exactly 4 times, but it was called 3 times");
  }

  @Test
  public void testMoxyAssertMockWithMockVoidMethodWasCalledExactNumberOfTimesWorksWithArgs() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("one", "two");

    mock.hasArgs("one", "three");

    mock.hasArgs("three", "four");

    Moxy.assertMock(() -> mock.hasArgs("one", "two")).wasCalled(2);
    Moxy.assertMock(() -> mock.hasArgs("one", "three")).wasCalled(1);
    Moxy.assertMock(() -> mock.hasArgs("three", "four")).wasCalled(1);
  }

  @Test
  public void testMoxyWhenWithMockCanUseSameSyntaxButCannotSetReturnForVoidMethod() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    /* Does not compile
    Moxy.when(() -> mock.hasArgs("one", "two")).thenReturn("anything");
    */

    final RuntimeException theException = new RuntimeException("Oops!");

    Moxy.when(() -> mock.hasArgs("one", "two")).thenThrow(theException);

    assertThatThrownBy(() -> mock.hasArgs("one", "two"))
        .isSameAs(theException);
  }

  @Test
  public void testMoxyMockWithMockWasNotCalledTakesAccountOfArguments() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    mock.hasArgs("this was", "called");

    Moxy.assertMock(() -> mock.hasArgs("this was", "called")).wasCalled();
    Moxy.assertMock(() -> mock.hasArgs("this was", "called")).wasCalled(1);

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.hasArgs("this was", "called")).wasNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock hasArgs(\"this was\", \"called\") to be called exactly zero times, but it was called once");

    Moxy.assertMock(() -> mock.hasArgs("was never", "called")).wasNotCalled();
  }

  @Test
  public void testMoxyMockWithMockWasCalledOnceWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    mock.returnShort();

    mock.returnDouble();
    mock.returnDouble();

    Moxy.assertMock(() -> mock.returnShort()).wasCalledOnce();

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnDouble()).wasCalledOnce()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnDouble() to be called exactly once, but it was called twice");

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnLong()).wasCalledOnce()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnLong() to be called exactly once, but it was called zero times");

  }

  @Test
  public void testMoxyMockWithMockWasCalledTwiceWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    mock.returnShort();

    mock.returnDouble();
    mock.returnDouble();

    Moxy.assertMock(() -> mock.returnDouble()).wasCalledTwice();

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnShort()).wasCalledTwice()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnShort() to be called exactly twice, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnLong()).wasCalledTwice()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnLong() to be called exactly twice, but it was called zero times");

  }

  @Test
  public void testMoxyMockWithMockWasCalledAtLeastWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    mock.returnShort();

    mock.returnDouble();
    mock.returnDouble();

    mock.returnBoolean();
    mock.returnBoolean();
    mock.returnBoolean();

    Moxy.assertMock(() -> mock.returnDouble()).wasCalledAtLeast(2);
    Moxy.assertMock(() -> mock.returnBoolean()).wasCalledAtLeast(2);

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnShort()).wasCalledAtLeast(2)
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnShort() to be called at least twice, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnLong()).wasCalledAtLeast(2)
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnLong() to be called at least twice, but it was called zero times");

  }

  @Test
  public void testMoxyMockWithMockWasCalledAtMostWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    mock.returnShort();

    mock.returnDouble();
    mock.returnDouble();

    mock.returnBoolean();
    mock.returnBoolean();
    mock.returnBoolean();

    // never called - passes
    Moxy.assertMock(() -> mock.returnLong()).wasCalledAtMost(1);

    Moxy.assertMock(() -> mock.returnShort()).wasCalledAtMost(1);
    Moxy.assertMock(() -> mock.returnDouble()).wasCalledAtMost(2);

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnBoolean()).wasCalledAtMost(2)
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnBoolean() to be called at most twice, but it was called 3 times");
  }

  @Test
  public void testMoxyMockWithMockWasCalledChainingWorks() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnHello())
            .wasCalledAtLeast(1)
            .wasCalledAtMost(3)
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello() to be called at least once, but it was called zero times");

    mock.returnHello();

    Moxy.assertMock(() -> mock.returnHello())
        .wasCalledAtLeast(1)
        .wasCalledAtMost(3);

    mock.returnHello();

    Moxy.assertMock(() -> mock.returnHello())
        .wasCalledAtLeast(1)
        .wasCalledAtMost(3);

    mock.returnHello();

    Moxy.assertMock(() -> mock.returnHello())
        .wasCalledAtLeast(1)
        .wasCalledAtMost(3);

    mock.returnHello();

    assertThatThrownBy(() ->
        Moxy.assertMock(() -> mock.returnHello())
            .wasCalledAtLeast(1)
            .wasCalledAtMost(3)
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello() to be called at most 3 times, but it was called 4 times");
  }

  @Test
  public void testCanStubAndAssertNullArgument() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo(null)).thenReturn("Hello, nobody");

    assertThat(mock.sayHelloTo(null)).isEqualTo("Hello, nobody");

    Moxy.assertMock(() -> mock.sayHelloTo(null)).wasCalledOnce();
  }

  @Test
  public void testMoxyWithMockAssertNeverThrewWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final RuntimeException marker = new RuntimeException("MARKER");

    Moxy.when(() -> mock.sayHelloTo("nothrow")).thenReturn("Didn't throw");
    Moxy.when(() -> mock.sayHelloTo("dothrow")).thenThrow(marker);

    mock.sayHelloTo("nothrow");

    assertThatThrownBy(() -> mock.sayHelloTo("dothrow")).isSameAs(marker);

    Moxy.assertMock(() -> mock.sayHelloTo("nothrow")).didntThrowAnyException();
    Moxy.assertMock(() -> mock.sayHelloTo("nothrow")).didntThrow(RuntimeException.class);
    Moxy.assertMock(() -> mock.sayHelloTo("nothrow")).didntThrow(marker);

    assertThatThrownBy(
          () -> Moxy.assertMock(() -> mock.sayHelloTo("dothrow")).didntThrowAnyException())
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(\"dothrow\") "
                + "never to throw any exception, but exceptions were thrown once");

    assertThatThrownBy(
          () -> Moxy.assertMock(() -> mock.sayHelloTo("dothrow")).didntThrow(RuntimeException.class))
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(\"dothrow\") "
                + "never to throw exception class java.lang.RuntimeException, but it was thrown once");

    assertThatThrownBy(
          () -> Moxy.assertMock(() -> mock.sayHelloTo("dothrow")).didntThrow(marker))
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(\"dothrow\") "
                + "never to throw exception java.lang.RuntimeException: MARKER, but it was thrown once");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksFailsWithNoInvocation() {
    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> { /* nothing happening here... */ })
    )
        .isInstanceOf(InvalidMockInvocationException.class)
        .hasMessage("No mock invocation found");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksReturnsMultiVerifier() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    final MoxyMultiVerifier multi = Moxy.assertMocks(() -> {
      mock.returnByte();
      mock.returnFloat();
    });

    assertThat(multi).isNotNull();
  }

  @Test
  public void testMoxyMockWithMockAssertMocksNoneCalledWorks() {
    Moxy.getMoxyEngine().reset();
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    // Note - this is testing that we don't just take arguments into account
    // when matching, but that we also take the method name/descriptor into account
    // too.
    mock.returnBoolean();
    mock.returnDouble();

    Moxy.assertMocks(() -> {
      mock.returnByte();
      mock.returnFloat();
    })
        .wereNotCalled();

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.returnByte();
          mock.returnBoolean();
          mock.returnFloat();
        })
            .wereNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnBoolean() to be called exactly zero times, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.returnDouble();
          mock.returnBoolean();
        })
            .wereNotCalled()
    )
        .isInstanceOf(MultipleFailuresError.class)
        .hasMessage("There were unexpected invocations (2 failures)" + EOL
            + "\tExpected mock returnDouble() to be called exactly zero times, but it was called once" + EOL
            + "\tExpected mock returnBoolean() to be called exactly zero times, but it was called once");

  }

  @Test
  public void testMoxyMockWithMockAssertMocksNoneCalledWithArgumentsWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 1);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Steve");
      mock.hasTwoArgs("two", 2);
    })
        .wereNotCalled();

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Bill");
          mock.hasTwoArgs("one", 1);
        })
            .wereNotCalled()
    )
        .isInstanceOf(MultipleFailuresError.class)
        .hasMessage("There were unexpected invocations (2 failures)" + EOL
                  + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly zero times, but it was called once" + EOL
                  + "\tExpected mock hasTwoArgs(\"one\", 1) to be called exactly zero times, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Bill");
          mock.hasTwoArgs("two", 2);
        })
            .wereNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock sayHelloTo(\"Bill\") to be called exactly zero times, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Steve");
          mock.hasTwoArgs("one", 1);
        })
            .wereNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock hasTwoArgs(\"one\", 1) to be called exactly zero times, but it was called once");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksNoneCalledWithAllPrimitiveTypes() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    mock.hasArgs("test", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false);

    Moxy.assertMocks(() -> {
      mock.hasArgs("TEST", (byte)2, 'b', (short)4, 5, 6L, 7.0f, 8.0d, true);
    }).wereNotCalled();

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.hasArgs("test", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false);
      }).wereNotCalled()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock hasArgs(\"test\", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false) to be called exactly zero times, but it was called once");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksAllCalledWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    mock.returnBoolean();
    mock.returnDouble();

    Moxy.assertMocks(() -> {
      mock.returnBoolean();
      mock.returnDouble();
    })
        .wereAllCalled();
  }
}
