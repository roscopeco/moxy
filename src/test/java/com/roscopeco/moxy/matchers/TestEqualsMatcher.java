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

import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestEqualsMatcher {
  @Test
  public void testMoxyMockVerifyWithEqualsByteMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(eqByte((byte)128))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsByteMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(eqByte(Byte.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(null);
    assertThat(mock.testByte((byte) 8)).isEqualTo(null);
    assertThat(mock.testByte((byte) 32)).isEqualTo(null);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsCharMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(eqChar('b'))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsCharMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(eqChar('c'))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(null);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(PASSED);
    assertThat(mock.testChar('x')).isEqualTo(null);
    assertThat(mock.testChar('y')).isEqualTo(null);
    assertThat(mock.testChar('z')).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsShortMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(eqShort(Short.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsShortMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(eqShort(Short.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(null);
    assertThat(mock.testShort((short) 1)).isEqualTo(null);
    assertThat(mock.testShort((short) 128)).isEqualTo(null);
    assertThat(mock.testShort((short) 256)).isEqualTo(null);
    assertThat(mock.testShort((short) 32767)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsIntMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(eqInt(Integer.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsIntMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(eqInt(Integer.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(null);
    assertThat(mock.testInt(1)).isEqualTo(null);
    assertThat(mock.testInt(256)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsLongMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(eqLong(Long.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsLongMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(eqLong(Long.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(null);
    assertThat(mock.testLong(1)).isEqualTo(null);
    assertThat(mock.testLong(256)).isEqualTo(null);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsFloatMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(eqFloat(Float.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsFloatMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(eqFloat(Float.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(null);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsDoubleMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(eqDouble(Double.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsDoubleMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(eqDouble(Double.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(null);
    assertThat(mock.testDouble(0.1d)).isEqualTo(null);
    assertThat(mock.testDouble(256.7d)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsBoolMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(eqBool(Boolean.TRUE))).wasCalled(2);
  }

  @Test
  public void testMoxyMockWhenWithEqualsBoolMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(eqBool(Boolean.TRUE))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsObjectMatcherWorks() {
    final MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");
    mock.hasArgs(null, "six");

    assertMock(() -> mock.hasArgs(eq("three"), eq("four"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(eq(null), eq("six"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(eq("one"), eq("four"))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithEqualsObjectMatcherWorks() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(eq("Steve"))).thenReturn(PASSED);
    when(() -> mock.sayHelloTo(eq(null))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);
    assertThat(mock.sayHelloTo(null)).isEqualTo(PASSED);
  }
}
