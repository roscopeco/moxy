package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.Matchers.not;
import static org.assertj.core.api.Assertions.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.PASSED;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestNotMatcher {
  @Test
  public void testMoxyMockVerifyWithNotByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(notByte(eqByte((byte)128)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(notByte(eqByte(Byte.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 1)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(PASSED);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(notChar(eqChar('b')))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(notChar(eqChar('c')))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(PASSED);
    assertThat(mock.testChar('b')).isEqualTo(PASSED);
    assertThat(mock.testChar('c')).isEqualTo(null);
    assertThat(mock.testChar('x')).isEqualTo(PASSED);
    assertThat(mock.testChar('y')).isEqualTo(PASSED);
    assertThat(mock.testChar('z')).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithNotShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(notShort(eqShort(Short.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(notShort(eqShort(Short.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 1)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(notInt(eqInt(Integer.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(notInt(eqInt(Integer.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(PASSED);
    assertThat(mock.testInt(1)).isEqualTo(PASSED);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(notLong(eqLong(Long.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(notLong(eqLong(Long.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(PASSED);
    assertThat(mock.testLong(1)).isEqualTo(PASSED);
    assertThat(mock.testLong(256)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(notFloat(eqFloat(Float.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(notFloat(eqFloat(Float.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(0.1f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(notDouble(eqDouble(Double.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(notDouble(eqDouble(Double.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(0.1d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(256.7d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(notBool(eqBool(Boolean.TRUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithNotBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(notBool(eqBool(Boolean.TRUE)))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(not(eq("three")), not(eq("four")))).wasCalledTwice();
    assertMock(() -> mock.hasArgs(not(eq("one")), not(eq("four")))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithNotObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(not(eq("Steve")))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(null);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Dave")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Carl")).isEqualTo(PASSED);
  }
}