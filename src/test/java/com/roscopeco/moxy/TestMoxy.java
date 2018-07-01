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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.example.model.FinalClass;
import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.InvalidStubbingException;
import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.model.ClassWithNoNullConstructor;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.FieldsClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleClass;
import com.roscopeco.moxy.model.SimpleInterface;

/*
 * This is basically integration tests. It's actually the tests used
 * for high-level TDD.
 *
 * We run it with the rest of the tests though as it's quick enough.
 *
 * If it gets much bigger it may need to be split up though.
 */
public class TestMoxy {
  private static final String PASSED = "passed";
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testMoxyMockWithNullThrowsIllegalArgumentException() {
    assertThatThrownBy(() -> Moxy.mock(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Cannot mock null");
  }

  @Test
  public void testMoxyMockWithSimpleClassReturnsInstance() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleClass.class);
  }

  @Test
  public void testMoxyMockWithSimpleClassReturnsActualMock() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock.returnHello())
        .isNull();
  }

  @Test
  public void testMoxyMockWithNullEngineUsesDefaultEngine() {
    Moxy.setMoxyEngine(null);

    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleClass.class);
  }

  @Test
  public void testMoxyMockHasIsMockAnnotation() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock.getClass()).hasAnnotation(Mock.class);
  }

  @Test
  public void testMoxyIsMockWorks() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(Moxy.isMock(mock.getClass())).isTrue();
    assertThat(Moxy.isMock(mock)).isTrue();
  }

  @Test
  public void testMoxyMockWithSimpleInterfaceReturnsInstance() {
    final SimpleInterface mock = Moxy.mock(SimpleInterface.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleInterface.class);
  }

  @Test
  public void testMoxyMockWithSimpleAbstractClassReturnsInstance() {
    final SimpleAbstractClass mock = Moxy.mock(SimpleAbstractClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleAbstractClass.class);
  }

  @Test
  public void testMoxyMockWithClassWithPrimitiveReturnsWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(ClassWithPrimitiveReturns.class);
  }

  @Test
  public void testMoxyMockWithMethodArgumentsWorks() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(MethodWithArguments.class);
  }

  @Test
  public void testMoxyMockWithPrimitiveMethodArgumentsWorks() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(MethodWithPrimitiveArguments.class);

    assertThat(mock.hasArgs("", (byte)1, '2', (short)3, 4, 5L, 6.0f, 7.0d, true))
        .isEqualTo(0);
  }

  @Test
  public void testMoxyWhenWithNoMockInvocationThrowsIllegalStateException() {
    assertThatThrownBy(() -> Moxy.when(() -> "Hello"))
        .isInstanceOf(InvalidMockInvocationException.class)
        .hasMessage("No mock invocation found");
  }

  @Test
  public void testMoxyWhenWithMockReturnsStubber() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);
    final MoxyStubber<String> stubber = Moxy.when(() -> mock.returnHello());

    assertThat(stubber)
        .isNotNull()
        .isInstanceOf(MoxyStubber.class);
  }

  @Test
  public void testMoxyWhenWithMockThenReturnForObjectWorksProperly() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);
    Moxy.when(() -> mock.returnHello()).thenReturn("Goodbye");

    assertThat(mock.returnHello()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyWhenWithMockThenReturnForPrimitiveWorksProperly() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    Moxy.when(() -> mock.returnInt()).thenReturn(0x2BADB002);
    Moxy.when(() -> mock.returnDouble()).thenReturn(4291.0d);

    assertThat(mock.returnInt()).isEqualTo(0x2BADB002);
    assertThat(mock.returnDouble()).isEqualTo(4291.0d);
  }

  /* No longer supported
  @Test
  public void testMoxyAssertMockWithNoMockInvocationThrowsIllegalStateException() {
    assertThatThrownBy(() -> Moxy.assertMock("Hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("No mock invocation found");
  }
  */

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
        .hasMessage("Expected mock hasArgs(java.lang.String, byte, char, short, int, long, float, double, boolean) to be called with arguments (\"Two\", 1, 'b', 10, 732819469, 200, 3579.0, 5302.0, false) at least once but it wasn't called at all");
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
  public void testMoxyAssertMockWithMockThenThrowWorks() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    final RuntimeException theException = new RuntimeException("Oops!");

    Moxy.when(() -> mock.returnHello()).thenThrow(theException);

    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }

  @Test
  public void testMoxyAssertMockWithMockThenThrowThenReturnFailsProperly() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    final RuntimeException theException = new RuntimeException("Oops!");

    assertThatThrownBy(() -> Moxy.when(() -> mock.returnHello()).thenThrow(theException).thenReturn("hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set return for 'java.lang.String returnHello()' as it has already been stubbed to throw or call real method");

    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }

  @Test
  public void testMoxyAssertMockWithMockThenReturnThenThrowFailsProperly() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    final RuntimeException theException = new RuntimeException("Oops!");

    assertThatThrownBy(() -> Moxy.when(() -> mock.returnHello()).thenReturn("hello").thenThrow(theException))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set throw for 'java.lang.String returnHello()' as it has already been stubbed to return or call real method");

    assertThat(mock.returnHello())
        .isEqualTo("hello");
  }

  @Test
  public void testMoxyAssertMockWithMockThenThrowThenSeparateThenReturnFailsProperly() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    final RuntimeException theException = new RuntimeException("Oops!");
    Moxy.when(() -> mock.returnHello()).thenThrow(theException);

    assertThatThrownBy(() -> Moxy.when(() -> mock.returnHello()).thenReturn("hello"))
        .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cannot set return for 'java.lang.String returnHello()' as it has already been stubbed to throw or call real method");

    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
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
  public void testMoxyMockWithMockWhenThenReturnTakesAccountOfArguments() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo("World")).thenReturn("Goodbye, cruel world");
    Moxy.when(() -> mock.sayHelloTo("Sam")).thenReturn("Oh hi, Sam!");

    assertThat(mock.sayHelloTo("World")).isEqualTo("Goodbye, cruel world");
    assertThat(mock.sayHelloTo("Sam")).isEqualTo("Oh hi, Sam!");
    assertThat(mock.sayHelloTo("Me")).isNull();
  }

  @Test
  public void testMoxyMockWithMockWhenThenThrowTakesAccountOfArguments() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final RuntimeException worldException = new RuntimeException("world");
    final RuntimeException samException = new RuntimeException("sam");

    Moxy.when(() -> mock.sayHelloTo("World")).thenThrow(worldException);
    Moxy.when(() -> mock.sayHelloTo("Sam")).thenThrow(samException);

    assertThatThrownBy(() -> mock.sayHelloTo("World")).isSameAs(worldException);
    assertThatThrownBy(() -> mock.sayHelloTo("Sam")).isSameAs(samException);
    assertThat(mock.sayHelloTo("Me")).isNull();
  }

  @Test
  public void testMoxyMockWithMockWhenThenThrowTakesAccountOfArgumentsWithVoidMethod() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    final RuntimeException worldException = new RuntimeException("world");
    final RuntimeException samException = new RuntimeException("sam");

    Moxy.when(() -> mock.hasArgs("hello", "world")).thenThrow(worldException);
    Moxy.when(() -> mock.hasArgs("hello", "sam")).thenThrow(samException);

    assertThatThrownBy(() -> mock.hasArgs("hello", "world")).isSameAs(worldException);
    assertThatThrownBy(() -> mock.hasArgs("hello", "sam")).isSameAs(samException);

    try {
      mock.hasArgs("hello", "matilda");
    } catch (final Throwable e) {
      throw new AssertionFailedError("Expected no exception but got " + e.getMessage());
    }
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
        .hasMessage("Expected mock hasArgs(java.lang.String, java.lang.String) to be called with arguments (\"this was\", \"called\") exactly zero times, but it was called once");

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

    Moxy.assertMock(() -> mock.sayHelloTo("nothrow")).neverThrewAnyException();
    Moxy.assertMock(() -> mock.sayHelloTo("nothrow")).neverThrew(RuntimeException.class);
    Moxy.assertMock(() -> mock.sayHelloTo("nothrow")).neverThrew(marker);

    assertThatThrownBy(
          () -> Moxy.assertMock(() -> mock.sayHelloTo("dothrow")).neverThrewAnyException())
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(java.lang.String) with arguments (\"dothrow\") "
                + "never to throw any exception, but exceptions were thrown once");

    assertThatThrownBy(
          () -> Moxy.assertMock(() -> mock.sayHelloTo("dothrow")).neverThrew(RuntimeException.class))
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(java.lang.String) with arguments (\"dothrow\") "
                + "never to throw exception class java.lang.RuntimeException, but it was thrown once");

    assertThatThrownBy(
          () -> Moxy.assertMock(() -> mock.sayHelloTo("dothrow")).neverThrew(marker))
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(java.lang.String) with arguments (\"dothrow\") "
                + "never to throw exception java.lang.RuntimeException: MARKER, but it was thrown once");
  }

  @Test
  public void testThatCanMockClassWithNoNullConstructor() {
    final ClassWithNoNullConstructor mock = Moxy.mock(ClassWithNoNullConstructor.class);

    Moxy.when(() -> mock.returnSomething()).thenReturn(PASSED);

    assertThat(mock.returnSomething()).isEqualTo(PASSED);
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

  @Test
  public void testMoxyMockGeneratesPassthroughConstructors() throws Exception {
    Class<? extends ClassWithNoNullConstructor> mockClass =
        Moxy.getMoxyEngine().getMockClass(
            ClassWithNoNullConstructor.class,
            Collections.singleton(ClassWithNoNullConstructor.class.getMethod("getAnyInt")));

    ClassWithNoNullConstructor mock =
        mockClass.getConstructor(MoxyEngine.class, String.class)
            .newInstance(Moxy.getMoxyEngine(), PASSED);

    assertThat(mock.returnSomething()).isEqualTo(PASSED);

    mockClass =
        Moxy.getMoxyEngine().getMockClass(
            ClassWithNoNullConstructor.class,
            Collections.singleton(ClassWithNoNullConstructor.class.getMethod("returnSomething")));

    mock = mockClass.getConstructor(MoxyEngine.class, int.class)
        .newInstance(Moxy.getMoxyEngine(), 37);

    assertThat(mock.getAnyInt()).isEqualTo(37);
  }

  @Test
  public void testMoxyMockDoesntCopyFields() throws Exception {
    final FieldsClass mock = Moxy.mock(FieldsClass.class);

    // Ensure we've only got the support fields, and haven't copied any...
    assertThat(mock.getClass().getDeclaredFields())
        .hasSize(4)
        .doesNotContainAnyElementsOf(
            Lists.newArrayList(
                FieldsClass.class.getDeclaredField("intField"),
                FieldsClass.class.getDeclaredField("byteField"),
                FieldsClass.class.getDeclaredField("boolField"),
                FieldsClass.class.getDeclaredField("longField")
            ));
  }

  @Test
  public void testMoxyMockCanMockJavaLangClasses() {
    final Object objectMock = Moxy.mock(Object.class);

    assertThat(objectMock)
        .isNotNull()
        .isInstanceOf(Object.class);
  }

  @Test
  public void testMoxyMockWithFinalClassFailsFast() {
    final FinalClass mock = Moxy.mock(FinalClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(FinalClass.class);
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
    Moxy.assertMock(() -> mock.sayHelloTo("Bill")).neverThrewAnyException();
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
        Moxy.assertMock(() -> mock.sayHelloTo("Hamburglar")).neverThrewAnyException()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock sayHelloTo(java.lang.String) with arguments (\"Hamburglar\") never to throw any exception, but exceptions were thrown once");
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

  @Test
  public void testMoxyMockThenStubThenResetWorksProperly() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final Exception ex = new Exception("No-one to say hello to");

    // Given I've stubbed some mocks...
    Moxy.when(() -> mock.sayHelloTo("Bill")).thenCallRealMethod();
    Moxy.when(() -> mock.sayHelloTo("Steve")).thenReturn("Oh hi, Steve");
    Moxy.when(() -> mock.sayHelloTo(null)).thenThrow(ex);

    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");
    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Oh hi, Steve");
    assertThatThrownBy(() ->
        mock.sayHelloTo(null)
    )
        .isSameAs(ex);

    // When I reset,
    Moxy.resetMock(mock);

    // Then the stubs are gone.
    assertThat(mock.sayHelloTo("Bill")).isNull();
    assertThat(mock.sayHelloTo("Steve")).isNull();
    assertThat(mock.sayHelloTo(null)).isNull();

    // And I can stub again.
    Moxy.when(() -> mock.sayHelloTo("Steve")).thenCallRealMethod();
    Moxy.when(() -> mock.sayHelloTo(null)).thenReturn("Oh hi, null");
    Moxy.when(() -> mock.sayHelloTo("Bill")).thenThrow(ex);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Hello, Steve");
    assertThat(mock.sayHelloTo(null)).isEqualTo("Oh hi, null");
    assertThatThrownBy(() ->
        mock.sayHelloTo("Bill")
    )
        .isSameAs(ex);
  }
}
