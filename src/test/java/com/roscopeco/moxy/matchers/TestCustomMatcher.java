package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static org.assertj.core.api.Assertions.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.PASSED;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestCustomMatcher {
  @Test
  public void testMoxyMockVerifyWithCustomByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(customByte((b) -> b.equals((byte)128)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(customByte((b) -> b.equals(Byte.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(null);
    assertThat(mock.testByte((byte) 8)).isEqualTo(null);
    assertThat(mock.testByte((byte) 32)).isEqualTo(null);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithCustomCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(customChar((c) -> c.equals('b')))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(customChar((c) -> c.equals('c')))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(null);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(PASSED);
    assertThat(mock.testChar('x')).isEqualTo(null);
    assertThat(mock.testChar('y')).isEqualTo(null);
    assertThat(mock.testChar('z')).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithCustomShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(customShort((s) -> s.equals(Short.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(customShort((s) -> s.equals(Short.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(null);
    assertThat(mock.testShort((short) 1)).isEqualTo(null);
    assertThat(mock.testShort((short) 128)).isEqualTo(null);
    assertThat(mock.testShort((short) 256)).isEqualTo(null);
    assertThat(mock.testShort((short) 32767)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithCustomIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(customInt((i) -> i.equals(Integer.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(customInt((i) -> i.equals(Integer.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(null);
    assertThat(mock.testInt(1)).isEqualTo(null);
    assertThat(mock.testInt(256)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(null);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithCustomLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(customLong((l) -> l.equals(Long.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(customLong((l) -> l.equals(Long.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(null);
    assertThat(mock.testLong(1)).isEqualTo(null);
    assertThat(mock.testLong(256)).isEqualTo(null);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithCustomFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(customFloat((f) -> f.equals(Float.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(customFloat((f) -> f.equals(Float.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(null);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithCustomDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(customDouble((d) -> d.equals(Double.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithCustomDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(customDouble((d) -> d.equals(Double.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(null);
    assertThat(mock.testDouble(0.1d)).isEqualTo(null);
    assertThat(mock.testDouble(256.7d)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithCustomBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(customBool((z) -> z.equals(Boolean.TRUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithCustomBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(customBool((z) -> z.equals(Boolean.TRUE)))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithCustomObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(
        () -> mock.hasArgs(
            custom((s) -> s.equals("three")), 
            custom((s) -> s.equals("four"))))
        .wasCalledOnce();
        
    assertMock(
        () -> mock.hasArgs(
            custom((s) -> s.equals("one")), 
            custom((s) -> s.equals("four"))))
        .wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithCustomObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(custom((s) -> s.equals("Steve")))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);
  }

  @Test
  public void testMoxyMockWhenWithCustomObjectMatcherFailsFastWithNull() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    assertThatThrownBy(() -> 
        when(() -> mock.sayHelloTo(custom(null))).thenReturn(PASSED)
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Null argument; see cause")
        .hasCauseInstanceOf(IllegalArgumentException.class);            
  }
}
