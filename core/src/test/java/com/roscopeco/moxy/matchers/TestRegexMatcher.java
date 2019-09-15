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

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import org.junit.jupiter.api.Test;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.regexMatch;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.PASSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestRegexMatcher {
    @Test
    void testMoxyMockVerifyWithRegexWithObjectMatcherWorks() {
        final MethodWithArguments mock = mock(MethodWithArguments.class);

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
    void testMoxyMockWhenWithRegexWithObjectMatcherWorks() {
        final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

        when(() -> mock.sayHelloTo(regexMatch("t[eo]ve$"))).thenReturn(PASSED);

        assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
        assertThat(mock.sayHelloTo("Stove")).isEqualTo(PASSED);
        assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);
        assertThat(mock.sayHelloTo(null)).isEqualTo(null);

        assertThatThrownBy(() ->
                when(() -> mock.sayHelloTo(regexMatch(null))).thenReturn(PASSED)
        )
                .isInstanceOf(MoxyException.class)
                .hasMessage("Null argument; see cause")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}
