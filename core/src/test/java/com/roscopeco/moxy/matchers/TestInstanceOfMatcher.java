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

public class TestInstanceOfMatcher {
  static class Super { }
  static final class SubOne extends Super { }
  static final class SubTwo extends Super { }

  @Test
  public void testMoxyMockVerifyWithInstanceOfMatcherWorks() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    mock.objectMethod(new Super());
    mock.objectMethod(new SubOne());
    mock.objectMethod(new SubTwo());

    assertMock(() -> mock.objectMethod(instanceOf(Super.class))).wasCalled(3);
    assertMock(() -> mock.objectMethod(instanceOf(SubOne.class))).wasCalled(1);
    assertMock(() -> mock.objectMethod(instanceOf(SubTwo.class))).wasCalled(1);

    assertThatThrownBy(() ->
        assertMock(() -> mock.objectMethod(startsWith(null))).wasNotCalled()
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testMoxyMockWhenWithInstanceOfMatcherWorks() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.objectMethod(instanceOf(Super.class))).thenReturn(PASSED);
    assertThat(mock.objectMethod(new Super())).isEqualTo(PASSED);
    assertThat(mock.objectMethod(new SubOne())).isEqualTo(PASSED);
    assertThat(mock.objectMethod(new SubTwo())).isEqualTo(PASSED);

    resetMock(mock);

    when(() -> mock.objectMethod(instanceOf(SubOne.class))).thenReturn(PASSED);
    assertThat(mock.objectMethod(new Super())).isEqualTo(null);
    assertThat(mock.objectMethod(new SubOne())).isEqualTo(PASSED);
    assertThat(mock.objectMethod(new SubTwo())).isEqualTo(null);

    resetMock(mock);

    when(() -> mock.objectMethod(instanceOf(SubTwo.class))).thenReturn(PASSED);
    assertThat(mock.objectMethod(new Super())).isEqualTo(null);
    assertThat(mock.objectMethod(new SubOne())).isEqualTo(null);
    assertThat(mock.objectMethod(new SubTwo())).isEqualTo(PASSED);

    assertThatThrownBy(() ->
        when(() -> mock.sayHelloTo(startsWith(null))).thenReturn(PASSED)
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);
  }
}
