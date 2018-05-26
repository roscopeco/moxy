package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestStartsWithMatcher {
  @Test
  public void testMoxyMockVerifyWithStartsWithObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(startsWith("t"), startsWith("f"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(startsWith("on"), startsWith("fo"))).wasNotCalled();

    assertThatThrownBy(() -> 
        assertMock(() -> mock.hasArgs(startsWith(null), startsWith(null))).wasNotCalled()
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);        
  }

  @Test
  public void testMoxyMockWhenWithStartsWithObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(startsWith("St"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);

    assertThatThrownBy(() -> 
        when(() -> mock.sayHelloTo(startsWith(null))).thenReturn(PASSED)
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);            
  }
}
