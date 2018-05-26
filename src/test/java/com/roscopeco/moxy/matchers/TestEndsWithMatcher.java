package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestEndsWithMatcher {
  @Test
  public void testMoxyMockVerifyWithEndsWithObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(endsWith("ne"), endsWith("wo"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(endsWith("e"), endsWith("wo"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(endsWith("ne"), endsWith("ur"))).wasNotCalled();
    assertThatThrownBy(() -> 

    assertMock(
        () -> mock.hasArgs(regexMatch(null), regexMatch(null))).wasNotCalled()
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);        
  }

  @Test
  public void testMoxyMockWhenWithEndsWithObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(endsWith("ve"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);

    assertThatThrownBy(() -> 
        when(() -> mock.sayHelloTo(endsWith(null))).thenReturn(PASSED)
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);            
  }
}
