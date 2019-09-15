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
import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import org.junit.jupiter.api.Test;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.PASSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestPredicateMatcher {
    @Test
    void testMoxyMockVerifyWithFunctionByteMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testByte((byte) 0);
        mock.testByte((byte) 128);
        mock.testByte((byte) 255);

        assertMock(() -> mock.testByte(matchesByte(b -> b.equals((byte) 128)))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionByteMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testByte(matchesByte(b -> b.equals(Byte.MAX_VALUE)))).thenReturn(PASSED);

        assertThat(mock.testByte((byte) 0)).isEqualTo(null);
        assertThat(mock.testByte((byte) 1)).isEqualTo(null);
        assertThat(mock.testByte((byte) 8)).isEqualTo(null);
        assertThat(mock.testByte((byte) 32)).isEqualTo(null);
        assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
    }

    @Test
    void testMoxyMockVerifyWithFunctionCharMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testChar('a');
        mock.testChar('b');
        mock.testChar('c');

        assertMock(() -> mock.testChar(matchesChar(c -> c.equals('b')))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionCharMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testChar(matchesChar(c -> c.equals('c')))).thenReturn(PASSED);

        assertThat(mock.testChar('a')).isEqualTo(null);
        assertThat(mock.testChar('b')).isEqualTo(null);
        assertThat(mock.testChar('c')).isEqualTo(PASSED);
        assertThat(mock.testChar('x')).isEqualTo(null);
        assertThat(mock.testChar('y')).isEqualTo(null);
        assertThat(mock.testChar('z')).isEqualTo(null);
    }

    @Test
    void testMoxyMockVerifyWithFunctionShortMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testShort((short) 0);
        mock.testShort((short) 256);
        mock.testShort(Short.MAX_VALUE);

        assertMock(() -> mock.testShort(matchesShort(s -> s.equals(Short.MAX_VALUE)))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionShortMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testShort(matchesShort(s -> s.equals(Short.MAX_VALUE)))).thenReturn(PASSED);

        assertThat(mock.testShort((short) 0)).isEqualTo(null);
        assertThat(mock.testShort((short) 1)).isEqualTo(null);
        assertThat(mock.testShort((short) 128)).isEqualTo(null);
        assertThat(mock.testShort((short) 256)).isEqualTo(null);
        assertThat(mock.testShort((short) 32767)).isEqualTo(PASSED);
    }

    @Test
    void testMoxyMockVerifyWithFunctionIntMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testInt(0);
        mock.testInt(1);
        mock.testInt(Integer.MAX_VALUE);

        assertMock(() -> mock.testInt(matchesInt(i -> i == Integer.MAX_VALUE))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionIntMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testInt(matchesInt(i -> i == Integer.MAX_VALUE))).thenReturn(PASSED);

        assertThat(mock.testInt(0)).isEqualTo(null);
        assertThat(mock.testInt(1)).isEqualTo(null);
        assertThat(mock.testInt(256)).isEqualTo(null);
        assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(null);
        assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(null);
        assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(PASSED);
    }

    @Test
    void testMoxyMockVerifyWithFunctionLongMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testLong(0);
        mock.testLong(1);
        mock.testLong(Long.MAX_VALUE);

        assertMock(() -> mock.testLong(matchesLong(j -> j == Long.MAX_VALUE))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionLongMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testLong(matchesLong(j -> j == Long.MAX_VALUE))).thenReturn(PASSED);

        assertThat(mock.testLong(0)).isEqualTo(null);
        assertThat(mock.testLong(1)).isEqualTo(null);
        assertThat(mock.testLong(256)).isEqualTo(null);
        assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(null);
        assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(null);
        assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(PASSED);
    }

    @Test
    void testMoxyMockVerifyWithFunctionFloatMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testFloat(0.0f);
        mock.testFloat(1.4f);
        mock.testFloat(Float.MAX_VALUE);

        assertMock(() -> mock.testFloat(matchesFloat(f -> f.equals(Float.MAX_VALUE)))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionFloatMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testFloat(matchesFloat(f -> f.equals(Float.MAX_VALUE)))).thenReturn(PASSED);

        assertThat(mock.testFloat(0.0f)).isEqualTo(null);
        assertThat(mock.testFloat(0.1f)).isEqualTo(null);
        assertThat(mock.testFloat(256.7f)).isEqualTo(null);
        assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
        assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
    }

    @Test
    void testMoxyMockVerifyWithFunctionDoubleMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testDouble(0.0d);
        mock.testDouble(1.4d);
        mock.testDouble(Double.MAX_VALUE);

        assertMock(() -> mock.testDouble(matchesDouble(d -> d == Double.MAX_VALUE))).wasCalledOnce();
    }

    @Test
    void testMoxyMockWhenWithFunctionDoubleMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testDouble(matchesDouble(d -> d == Double.MAX_VALUE))).thenReturn(PASSED);

        assertThat(mock.testDouble(0.0d)).isEqualTo(null);
        assertThat(mock.testDouble(0.1d)).isEqualTo(null);
        assertThat(mock.testDouble(256.7d)).isEqualTo(null);
        assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
        assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
    }

    @Test
    void testMoxyMockVerifyWithFunctionBoolMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        mock.testBoolean(Boolean.TRUE);
        mock.testBoolean(Boolean.FALSE);
        mock.testBoolean(Boolean.TRUE);

        assertMock(() -> mock.testBoolean(matchesBool(z -> z.equals(Boolean.TRUE)))).wasCalledTwice();
    }

    @Test
    void testMoxyMockWhenWithFunctionBoolMatcherWorks() {
        final MatcherTestClass mock = mock(MatcherTestClass.class);

        when(() -> mock.testBoolean(matchesBool(z -> z.equals(Boolean.TRUE)))).thenReturn(PASSED);

        assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
        assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(null);
    }

    @Test
    void testMoxyMockVerifyWithFunctionObjectMatcherWorks() {
        final MethodWithArguments mock = mock(MethodWithArguments.class);

        mock.hasArgs("one", "two");
        mock.hasArgs("three", "four");
        mock.hasArgs("five", "six");

        assertMock(
                () -> mock.hasArgs(
                        matches(s -> s.equals("three")),
                        matches(s -> s.equals("four"))))
                .wasCalledOnce();

        assertMock(
                () -> mock.hasArgs(
                        matches(s -> s.equals("one")),
                        matches(s -> s.equals("four"))))
                .wasNotCalled();
    }

    @Test
    void testMoxyMockWhenWithFunctionObjectMatcherWorks() {
        final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

        when(() -> mock.sayHelloTo(matches(s -> s.equals("Steve")))).thenReturn(PASSED);

        assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
        assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);
    }

    @Test
    void testMoxyMockWhenWithCustomObjectMatcherFailsFastWithNull() {
        final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

        assertThatThrownBy(() ->
                when(() -> mock.sayHelloTo(custom(null))).thenReturn(PASSED)
        )
                .isInstanceOf(MoxyException.class)
                .hasMessage("Null argument; see cause")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}
