package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleClass;
import com.roscopeco.moxy.model.SimpleInterface;

import junit.framework.AssertionFailedError;

public class TestMoxy {
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().getRecorder().reset();
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
    SimpleInterface mock = Moxy.mock(SimpleInterface.class);
    
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
    assertThatThrownBy(() -> Moxy.when("Hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("No mock invocation found");
  }
  
  @Test
  public void testMoxyWhenWithMockReturnsStubber() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    MoxyStubber<String> stubber = Moxy.when(mock.returnHello());
    
    assertThat(stubber)
        .isNotNull()
        .isInstanceOf(MoxyStubber.class);
  }
  
  @Test
  public void testMoxyWhenWithMockThenReturnForObjectWorksProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    Moxy.when(mock.returnHello()).thenReturn("Goodbye");
    
    assertThat(mock.returnHello()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyWhenWithMockThenReturnForPrimitiveWorksProperly() {
    ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);
    Moxy.when(mock.returnInt()).thenReturn(0x2BADB002);
    Moxy.when(mock.returnDouble()).thenReturn(4291.0d);
    
    assertThat(mock.returnInt()).isEqualTo(0x2BADB002);
    assertThat(mock.returnDouble()).isEqualTo(4291.0d);
  }
  
  @Test
  public void testMoxyAssertMockWithNoMockInvocationThrowsIllegalStateException() {
    assertThatThrownBy(() -> Moxy.assertMock("Hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("No mock invocation found");
  }
  
  @Test
  public void testMoxyAssertMockWithMockReturnsVerifier() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    MoxyVerifier<String> verifier = Moxy.assertMock(mock.returnHello());
    
    assertThat(verifier)
        .isNotNull()
        .isInstanceOf(MoxyVerifier.class);
  }
  
  @Test
  public void testMoxyAssertMockWithMockWasCalledFailsIfNotCalled() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    assertThatThrownBy(() -> Moxy.assertMock(mock.returnHello()).wasCalled())
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello(...) to be called with arguments () at least once but it wasn't");
  }

  /* We're bootstrapped. Let's start eating our own dog food... */
  @Test
  public void testMoxyAssertMockWithMockWasCalledWorksIfWasCalled() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    mock.returnHello();
    
    Moxy.assertMock(mock.returnHello()).wasCalled();
  }
  
  @Test
  public void testMoxyAssertMockWithMockWasCalledWithExactArgumentsWorks() {
    MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);
    
    mock.hasArgs("One", (byte)2, 'a', (short)20, 0xdeadbeef, 100L, 2468.0f, 4291.0d, true);
    
    Moxy.assertMock(mock.hasArgs("One", (byte)2, 'a', (short)20, 0xdeadbeef, 100L, 2468.0f, 4291.0d, true))
        .wasCalled();
    
    assertThatThrownBy(() -> 
        Moxy.assertMock(mock.hasArgs("Two", (byte)1, 'b', (short)10, 0x2badf00d, 200L, 3579.0f, 5302.0d, false))
            .wasCalled())
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock hasArgs(...) to be called with arguments (Two,1,b,10,732819469,200,3579.0,5302.0,false) at least once but it wasn't");
  }
  
  @Test
  public void testMoxyAssertMockWithMockWasCalledExactNumberOfTimesWorks() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    mock.returnHello();    
    Moxy.assertMock(mock.returnHello()).wasCalled(1);

    mock.returnHello();    
    Moxy.assertMock(mock.returnHello()).wasCalled(2);
    
    mock.returnHello();    
    Moxy.assertMock(mock.returnHello()).wasCalled(3);

    assertThatThrownBy(() -> 
        Moxy.assertMock(mock.returnHello())
            .wasCalled(4))
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnHello(...) to be called with arguments () exactly 4 time(s) but it wasn't");

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
    
    Moxy.when(mock.returnHello()).thenThrow(theException);
    
    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }
  
  @Test
  public void testMoxyAssertMockWithMockThenThrowThenReturnFailsProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    
    assertThatThrownBy(() -> Moxy.when(mock.returnHello()).thenThrow(theException).thenReturn("hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set both throw and return for returnHello()Ljava/lang/String;");
    
    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }
  
  @Test
  public void testMoxyAssertMockWithMockThenReturnThenThrowFailsProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    
    assertThatThrownBy(() -> Moxy.when(mock.returnHello()).thenReturn("hello").thenThrow(theException))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set both throw and return for returnHello()Ljava/lang/String;");
    
    assertThat(mock.returnHello())
        .isEqualTo("hello");
  }
  
  // BUG: This can't work with current setup, exception will leak from invocation.
  //
  // FIX: Move to lambda based invocations across the board with a custom functional to swallow? 
  @Test
  public void testMoxyAssertMockWithMockThenThrowThenSeparateThenReturnFailsProperly() {
    SimpleClass mock = Moxy.mock(SimpleClass.class);
    
    RuntimeException theException = new RuntimeException("Oops!");
    Moxy.when(mock.returnHello()).thenThrow(theException);
    
    assertThatThrownBy(() -> Moxy.when(mock.returnHello()).thenReturn("hello"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set both throw and return for returnHello()Ljava/lang/String;");
    
    assertThatThrownBy(() -> mock.returnHello())
        .isSameAs(theException);
  }
}
