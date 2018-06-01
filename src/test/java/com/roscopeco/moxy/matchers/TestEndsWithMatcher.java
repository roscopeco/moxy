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
    final MethodWithArguments mock = mock(MethodWithArguments.class);

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
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

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
