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
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;

public abstract class TestMoxyMatchers {
  public static final String PASSED = "passed";

  @Test
  public void testMoxyMockWhenWithMatcherCanStubMultipleTimes() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(eq("Steve"))).thenReturn("Hello, Steve");
    when(() -> mock.sayHelloTo(eq("Bill"))).thenReturn("Hello, Bill");

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Hello, Steve");
    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");
    assertThat(mock.sayHelloTo("Sam")).isEqualTo(null);
  }

  @Test
  public void testMoxyMockWhenWithMatcherFailsFastIfMixed() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

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
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

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
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(anyByte())).thenReturn(PASSED);
    when(() -> mock.testBoolean(anyBool())).thenReturn(PASSED);

    assertThat(mock.testBoolean(true)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)10)).isEqualTo(PASSED);

    assertMock(() -> mock.testByte((byte)10)).wasCalledOnce();
    assertMock(() -> mock.testBoolean(true)).wasCalledOnce();
  }

  @Test
  public void testThatMockMockWithMatcherUsageErrorThrowsProperly() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    assertThatThrownBy(() ->
        Moxy.when(() -> mock.hasTwoArgs(Matchers.any(), 5))
    )
        .isInstanceOf(InconsistentMatchersException.class)
        .hasMessage("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
            + "This limitation will (hopefully) be lifted in the future");

    assertThatThrownBy(() ->
        Moxy.when(() -> mock.hasTwoArgs("illegal bare arg", Matchers.anyInt()))
    )
        .isInstanceOf(InconsistentMatchersException.class)
        .hasMessage("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
            + "This limitation will (hopefully) be lifted in the future");

    assertThatThrownBy(() ->
        Moxy.when(() -> mock.hasTwoArgs(Matchers.any(), Matchers.any()))
    )
      .isInstanceOf(PossibleMatcherUsageError.class)
      .hasMessage("If you're using primitive matchers, ensure you're using the "
          + "correct type (e.g. anyInt() rather than any()), especially when nesting");
  }
}
