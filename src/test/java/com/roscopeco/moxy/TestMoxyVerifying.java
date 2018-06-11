package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleClass;

public class TestMoxyVerifying {
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
}
