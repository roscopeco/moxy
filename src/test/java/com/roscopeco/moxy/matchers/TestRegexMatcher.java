package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestRegexMatcher {
  @Test
  public void testMoxyMockVerifyWithRegexWithObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(
        () -> mock.hasArgs(regexMatch("[ot][nh][er]e?e?"), regexMatch("(two|four)")))
      .wasCalledTwice();
    
    assertMock(() -> mock.hasArgs(regexMatch("e$"), regexMatch("o$"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(regexMatch("ne$"), regexMatch("ur$"))).wasNotCalled();

    assertThatThrownBy(() -> 
        assertMock(() -> mock.hasArgs(regexMatch(null), regexMatch(null))).wasNotCalled()
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);        
  }

  @Test
  public void testMoxyMockWhenWithRegexWithObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(regexMatch("t[eo]ve$"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Stove")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);
    assertThat(mock.sayHelloTo(null)).isEqualTo(null);
  }
}
