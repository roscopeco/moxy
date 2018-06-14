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

public class TestNotEqualsMatcher {
  @Test
  public void testMoxyMockVerifyWithNotEqualsByteMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(neqByte((byte)128))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsByteMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(neqByte(Byte.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 1)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(PASSED);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsCharMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(neqChar('b'))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsCharMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(neqChar('c'))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(PASSED);
    assertThat(mock.testChar('b')).isEqualTo(PASSED);
    assertThat(mock.testChar('c')).isEqualTo(null);
    assertThat(mock.testChar('x')).isEqualTo(PASSED);
    assertThat(mock.testChar('y')).isEqualTo(PASSED);
    assertThat(mock.testChar('z')).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsShortMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(neqShort(Short.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsShortMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(neqShort(Short.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 1)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsIntMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(neqInt(Integer.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsIntMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(neqInt(Integer.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(PASSED);
    assertThat(mock.testInt(1)).isEqualTo(PASSED);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsLongMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(neqLong(Long.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsLongMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(neqLong(Long.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(PASSED);
    assertThat(mock.testLong(1)).isEqualTo(PASSED);
    assertThat(mock.testLong(256)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsFloatMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(neqFloat(Float.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsFloatMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(neqFloat(Float.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(0.1f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsDoubleMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(neqDouble(Double.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsDoubleMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(neqDouble(Double.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(0.1d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(256.7d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsBoolMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(neqBool(Boolean.TRUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsBoolMatcherWorks() {
    final MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(neqBool(Boolean.TRUE))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(null);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsObjectMatcherWorks() {
    final MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(neq("three"), neq("four"))).wasCalledTwice();
    assertMock(() -> mock.hasArgs(neq("one"), neq("four"))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsObjectMatcherWorksWithNull() {
    final MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(neq(null), neq(null))).wasCalled(3);
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsObjectMatcherWorks() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(neq("Steve"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(null);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsObjectMatcherWorksWithNull() {
    final MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(neq(null))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(PASSED);
  }
}
