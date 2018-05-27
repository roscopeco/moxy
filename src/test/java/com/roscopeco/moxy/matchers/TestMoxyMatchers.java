package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;

public abstract class TestMoxyMatchers {
  public static final String PASSED = "passed";

  @Test
  public void testMoxyMockWhenWithMatcherCanStubMultipleTimes() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(eq("Steve"))).thenReturn("Hello, Steve");
    when(() -> mock.sayHelloTo(eq("Bill"))).thenReturn("Hello, Bill");

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Hello, Steve");
    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");
    assertThat(mock.sayHelloTo("Sam")).isEqualTo(null);
  }
  
  @Test
  public void testMoxyMockWhenWithMatcherFailsFastIfMixed() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    assertThatThrownBy(() -> 
        when(() -> mock.hasTwoArgs("Hello", anyInt())).thenReturn(PASSED)
    )
        .isInstanceOf(InconsistentMatchersException.class)
        .hasMessage("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
                  + "This limitation will (hopefully) be lifted in the future.");

    assertThatThrownBy(() -> 
        when(() -> mock.hasTwoArgs(any(), 42)).thenReturn(PASSED)
    )
        .isInstanceOf(InconsistentMatchersException.class)
        .hasMessage("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
                  + "This limitation will (hopefully) be lifted in the future.");

    // Stubbing failed, so both of these should return null
    assertThat(mock.hasTwoArgs("Hello", 42)).isEqualTo(null);
    assertThat(mock.hasTwoArgs("Goodbye", 0xdead)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockAssertWithMatcherFailsFastIfMixed() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    mock.hasTwoArgs("one", 2);
    
    assertThatThrownBy(() -> 
      assertMock(() -> mock.hasTwoArgs("Hello", anyInt())).wasCalledOnce()
    )
        .isInstanceOf(InconsistentMatchersException.class)
        .hasMessage("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
                  + "This limitation will (hopefully) be lifted in the future.");

    assertThatThrownBy(() -> 
      assertMock(() -> mock.hasTwoArgs(any(), 42)).wasCalledOnce()
    )
        .isInstanceOf(InconsistentMatchersException.class)
        .hasMessage("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
                  + "This limitation will (hopefully) be lifted in the future.");

  }
  
  @Test
  public void testMoxyMockWithMockWhenCanStubMultipleCallsBeforeInvocation() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(anyByte())).thenReturn(PASSED);
    when(() -> mock.testBoolean(anyBool())).thenReturn(PASSED);
    
    assertThat(mock.testBoolean(true)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)10)).isEqualTo(PASSED);
    
    assertMock(() -> mock.testByte((byte)10)).wasCalledOnce();
    assertMock(() -> mock.testBoolean(true)).wasCalledOnce();
  }
}
