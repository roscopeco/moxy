package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.SimpleClass;

public class TestMoxyStubbing {
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
  public void testMoxyMockWithMockWhenThenReturnTakesAccountOfArguments() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo("World")).thenReturn("Goodbye, cruel world");
    Moxy.when(() -> mock.sayHelloTo("Sam")).thenReturn("Oh hi, Sam!");

    assertThat(mock.sayHelloTo("World")).isEqualTo("Goodbye, cruel world");
    assertThat(mock.sayHelloTo("Sam")).isEqualTo("Oh hi, Sam!");
    assertThat(mock.sayHelloTo("Me")).isNull();
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
}
