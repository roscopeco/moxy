package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.Matchers.anyOf;
import static org.assertj.core.api.Assertions.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.PASSED;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestAnyOfMatcher {
  @Test
  public void testMoxyMockVerifyWithAnyOfByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(anyOfByte((byte)0, (byte)128, (byte)129))).wasCalledTwice();
    assertMock(() -> mock.testByte(anyOfByte((byte)1, (byte)129, (byte)42))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(anyOfByte((byte)1, (byte)8, Byte.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(null);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(anyOfChar('a', 'c'))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(anyOfChar('a', 'x', 'z'))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(PASSED);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(null);
    assertThat(mock.testChar('x')).isEqualTo(PASSED);
    assertThat(mock.testChar('y')).isEqualTo(null);
    assertThat(mock.testChar('z')).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(anyOfShort((short)0, (short)127, Short.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(anyOfShort((short)0, (short)1, Short.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 1)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 128)).isEqualTo(null);
    assertThat(mock.testShort((short) 256)).isEqualTo(null);
    assertThat(mock.testShort((short) 32767)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(anyOfInt(0, 42, 256, Integer.MAX_VALUE))).wasCalledTwice();
    assertMock(() -> mock.testInt(anyOfInt(42, 256))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(anyOfInt(0, 256, Integer.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(PASSED);
    assertThat(mock.testInt(1)).isEqualTo(null);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(anyOfLong(0l, Long.MAX_VALUE))).wasCalledTwice();
    assertMock(() -> mock.testLong(anyOfLong(2l, Long.MIN_VALUE))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(anyOfLong(0l, 256l, Long.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(PASSED);
    assertThat(mock.testLong(1)).isEqualTo(null);
    assertThat(mock.testLong(256)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(anyOfFloat(1.4f, Float.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(anyOfFloat(0.0f, Float.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(anyOfDouble(0.0d, Double.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(anyOfDouble(0.0d, Double.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(0.1d)).isEqualTo(null);
    assertThat(mock.testDouble(256.7d)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(anyOfBool(true, false))).wasCalled(3);
    assertMock(() -> mock.testBoolean(anyOfBool(true))).wasCalled(2);
  }

  @Test
  public void testMoxyMockWhenWithAnyOfBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(anyOfBool(true, false))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyOfObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(anyOf("one", "five"), anyOf("two", "six"))).wasCalledTwice();
    assertMock(() -> mock.hasArgs(anyOf("ten", "eleven"), anyOf("twelve", "fourteen"))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithAnyOfObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(anyOf("Steve", "Bill"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Sam")).isEqualTo(null);
  }
}