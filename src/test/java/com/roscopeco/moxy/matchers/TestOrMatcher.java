package com.roscopeco.moxy.matchers;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.TestMoxyMatchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestOrMatcher {
  @Test
  public void testMoxyMockVerifyWithOrByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 63);
    mock.testByte((byte) 64);
    mock.testByte((byte) 126);
    mock.testByte((byte) 127);

    assertMock(() -> mock.testByte(orByte(eqByte((byte)63), eqByte((byte)127)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(orByte(eqByte((byte)8), eqByte(Byte.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(null);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(null);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithOrCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(orChar(eqChar('a'), eqChar('c')))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(orChar(eqChar('c'), eqChar('f')))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(null);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(PASSED);
    assertThat(mock.testChar('d')).isEqualTo(null);
    assertThat(mock.testChar('e')).isEqualTo(null);
    assertThat(mock.testChar('f')).isEqualTo(PASSED);

    // Can use more than two options...
    when(() -> mock.testChar(orChar(eqChar('a'), eqChar('c'), eqChar('f')))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(PASSED);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(PASSED);
    assertThat(mock.testChar('d')).isEqualTo(null);
    assertThat(mock.testChar('e')).isEqualTo(null);
    assertThat(mock.testChar('f')).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithOrShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 1);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(orShort(eqShort((short)0), eqShort(Short.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(orShort(eqShort((short)128), eqShort((short)256)))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(null);
    assertThat(mock.testShort((short) 1)).isEqualTo(null);
    assertThat(mock.testShort((short) 128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithOrIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(2);
    mock.testInt(Integer.MAX_VALUE - 1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(orInt(eqInt(1), eqInt(Integer.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(orInt(eqInt(256), eqInt(Integer.MAX_VALUE - 1)))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(null);
    assertThat(mock.testInt(1)).isEqualTo(null);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithOrLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(2);
    mock.testLong(99);
    mock.testLong(100);

    assertMock(() -> mock.testLong(orLong(eqLong(1), eqLong(100)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(orLong(eqLong(11), eqLong(256), eqLong(999)))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(null);
    assertThat(mock.testLong(1)).isEqualTo(null);
    assertThat(mock.testLong(11)).isEqualTo(PASSED);
    assertThat(mock.testLong(256)).isEqualTo(PASSED);
    assertThat(mock.testLong(999)).isEqualTo(PASSED);
    assertThat(mock.testLong(1000)).isEqualTo(null);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithOrFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(orFloat(eqFloat(1.4f), ltFloat(Float.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(orFloat(eqFloat(256.7f), eqFloat(Float.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(null);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithOrDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(orDouble(eqDouble(1.4d), eqDouble(Double.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(orDouble(eqDouble(6.0d), eqDouble(9.0d)))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(null);
    assertThat(mock.testDouble(6.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(9.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(null);
  }
  
  // TODO test Bool match when we've got matchers that make sense to OR for bools.

  @Test
  public void testMoxyMockVerifyWithOrObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("AAAA", "DDDD");
    mock.hasArgs("BBBB", "DDDD");
    mock.hasArgs("AAAA", "No match");
    mock.hasArgs("CCCC", "DDDD");

    assertMock(() -> mock.hasArgs(or(eq("AAAA"), eq("CCCC")), eq("DDDD"))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithOrObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(or(eq("BBBB"), eq("CCCC")))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("AAAA")).isEqualTo(null);
    assertThat(mock.sayHelloTo("BBBB")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("CCCC")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("DDDD")).isEqualTo(null);
  }
}
