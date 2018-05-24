package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleClass;
import com.roscopeco.moxy.model.SimpleInterface;

import junit.framework.AssertionFailedError;

public class TestMoxy {
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
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleClass.class);
  }
  
  @Test
  public void testMoxyMockWithSimpleClassReturnsActualMock() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThat(mock.returnHello())
        .isNull();
  }
  
  @Test
  public void testMoxyMockWithNullEngineUsesDefaultEngine() {
    Moxy.setMoxyEngine(null);

    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleClass.class);
  }  
  
  @Test
  public void testMoxyMockHasIsMockAnnotation() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThat(mock.getClass()).hasAnnotation(Mock.class);    
  }

  @Test
  public void testMoxyIsMockWorks() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThat(Moxy.isMock(mock.getClass())).isTrue();
    assertThat(Moxy.isMock(mock)).isTrue();
  }
  
  @Test
  public void testMoxyMockWithSimpleInterfaceReturnsInstance() {
    SimpleInterface mock = Moxy.mock(SimpleInterface.class, System.out);
    
    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleInterface.class);
  }

  @Test
  public void testMoxyMockWithSimpleAbstractClassReturnsInstance() {
    SimpleAbstractClass mock = Moxy.mock(SimpleAbstractClass.class);
    
    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleAbstractClass.class);
  }
  
  @Test
  public void testMoxyMockWithClassWithPrimitiveReturnsWorks() {
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    
    assertThat(mock)
        .isNotNull()
        .isInstanceOf(ClassWithPrimitiveReturns.class);
  }
  
  @Test
  public void testMoxyMockWithMethodArgumentsWorks() {
    MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(MethodWithArguments.class);
  }
  
  @Test
  public void testMoxyMockWithPrimitiveMethodArgumentsWorks() {
    MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(MethodWithPrimitiveArguments.class);
    
    assertThat(mock.hasArgs("", (byte)1, '2', (short)3, 4, 5L, 6.0f, 7.0d, true))
        .isEqualTo(0);
  }
    
  @Test
  public void testMoxyWhenWithNoMockInvocationThrowsIllegalStateException() {
    assertThatThrownBy(() -> Moxy.when(() -> "Hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("No mock invocation found");
  }
  
  @Test
  public void testMoxyWhenWithMockReturnsStubber() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    MoxyStubber<String> stubber = Moxy.when(() -> mock.returnHello());
    
    assertThat(stubber)
        .isNotNull()
        .isInstanceOf(MoxyStubber.class);
  }
  
  @Test
  public void testMoxyWhenWithMockThenReturnForObjectWorksProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    Moxy.when(() -> mock.returnHello()).thenReturn("Goodbye");
    
    assertThat(mock.returnHello()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyWhenWithMockThenReturnForPrimitiveWorksProperly() {
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
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
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    MoxyVerifier verifier = Moxy.assertMock(() -> mock.returnHello());
    
    assertThat(verifier)
        .isNotNull()
        .isInstanceOf(MoxyVerifier.class);
  }
  
  @Test
  public void testMoxyAssertMockWithMockWasCalledFailsIfNotCalled() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThatThrownBy(() -> Moxy.assertMock(() -> mock.returnHello()).wasCalled())
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello() to be called at least once but it wasn't called at all");
  }

  /* We're bootstrapped. Let's start eating our own dog food... */
  @Test
  public void testMoxyAssertMockWithMockWasCalledWorksIfWasCalled() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    mock.returnHello();
    
    Moxy.assertMock(() -> mock.returnHello()).wasCalled();
  }
  
  @Test
  public void testMoxyAssertMockWithMockWasCalledWithExactArgumentsWorks() {
    MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);
    
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
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
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
    MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);
    
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
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    
    Moxy.when(() -> mock.returnHello()).thenThrow(theException);
    
    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }
  
  @Test
  public void testMoxyAssertMockWithMockThenThrowThenReturnFailsProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    
    assertThatThrownBy(() -> Moxy.when(() -> mock.returnHello()).thenThrow(theException).thenReturn("hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set both throw and return for returnHello()Ljava/lang/String;");
    
    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }
  
  @Test
  public void testMoxyAssertMockWithMockThenReturnThenThrowFailsProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    
    assertThatThrownBy(() -> Moxy.when(() -> mock.returnHello()).thenReturn("hello").thenThrow(theException))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set both throw and return for returnHello()Ljava/lang/String;");
    
    assertThat(mock.returnHello())
        .isEqualTo("hello");
  }
  
  @Test
  public void testMoxyAssertMockWithMockThenThrowThenSeparateThenReturnFailsProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    Moxy.when(() -> mock.returnHello()).thenThrow(theException);
    
    assertThatThrownBy(() -> Moxy.when(() -> mock.returnHello()).thenReturn("hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set both throw and return for returnHello()Ljava/lang/String;");
    
    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }
  
  @Test
  public void testMoxyWhenWithMockCanUseSameSyntaxButCannotSetReturnForVoidMethod() {
    MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);
    
    /* Does not compile
    Moxy.when(() -> mock.hasArgs("one", "two")).thenReturn("anything");
    */
    
    RuntimeException theException = new RuntimeException("Oops!");
    
    Moxy.when(() -> mock.hasArgs("one", "two")).thenThrow(theException);
    
    assertThatThrownBy(() -> mock.hasArgs("one", "two"))
        .isSameAs(theException);
  }
  
  @Test
  public void testMoxyMockWithMockWhenThenReturnTakesAccountOfArguments() {
    MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);
    
    Moxy.when(() -> mock.sayHelloTo("World")).thenReturn("Goodbye, cruel world");
    Moxy.when(() -> mock.sayHelloTo("Sam")).thenReturn("Oh hi, Sam!");
    
    assertThat(mock.sayHelloTo("World")).isEqualTo("Goodbye, cruel world");
    assertThat(mock.sayHelloTo("Sam")).isEqualTo("Oh hi, Sam!");
    assertThat(mock.sayHelloTo("Me")).isNull();
  }

  @Test
  public void testMoxyMockWithMockWhenThenThrowTakesAccountOfArguments() {
    MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);
    
    RuntimeException worldException = new RuntimeException("world");
    RuntimeException samException = new RuntimeException("sam");
    
    Moxy.when(() -> mock.sayHelloTo("World")).thenThrow(worldException);
    Moxy.when(() -> mock.sayHelloTo("Sam")).thenThrow(samException);
    
    assertThatThrownBy(() -> mock.sayHelloTo("World")).isSameAs(worldException);
    assertThatThrownBy(() -> mock.sayHelloTo("Sam")).isSameAs(samException);
    assertThat(mock.sayHelloTo("Me")).isNull();
  }

  @Test
  public void testMoxyMockWithMockWhenThenThrowTakesAccountOfArgumentsWithVoidMethod() {
    MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);
    
    RuntimeException worldException = new RuntimeException("world");
    RuntimeException samException = new RuntimeException("sam");
    
    Moxy.when(() -> mock.hasArgs("hello", "world")).thenThrow(worldException);
    Moxy.when(() -> mock.hasArgs("hello", "sam")).thenThrow(samException);
    
    assertThatThrownBy(() -> mock.hasArgs("hello", "world")).isSameAs(worldException);
    assertThatThrownBy(() -> mock.hasArgs("hello", "sam")).isSameAs(samException);

    try {
      mock.hasArgs("hello", "matilda");
    } catch (Throwable e) {
      throw new AssertionFailedError("Expected no exception but got " + e.getMessage());
    }
  }
  
  @Test
  public void testMoxyMockWithMockWasNotCalledTakesAccountOfArguments() {
    MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);
    
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
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    
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
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    
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
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    
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
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    
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
    SimpleClass mock = Moxy.mock(SimpleClass.class);

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
}
