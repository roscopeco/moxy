package com.roscopeco.moxy;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static com.roscopeco.moxy.matchers.Matchers.not;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

// TODO this really needs splitting up...
public class TestMoxyMatchers {
  private static final String PASSED = "passed";

  @Test
  public void testMoxyMockVerifyWithAnyByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testByte((byte)0);
    mock.testByte((byte)128);
    mock.testByte((byte)255);
        
    assertMock(() -> mock.testByte(anyByte())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(anyByte())).thenReturn(PASSED);
    
    assertThat(mock.testByte((byte)0)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)1)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)32)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)128)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)255)).isEqualTo(PASSED);    
  }

  @Test
  public void testMoxyMockVerifyWithAnyCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');
        
    assertMock(() -> mock.testChar(anyChar())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testChar(anyChar())).thenReturn(PASSED);
    
    assertThat(mock.testChar('a')).isEqualTo(PASSED);
    assertThat(mock.testChar('b')).isEqualTo(PASSED);
    assertThat(mock.testChar('c')).isEqualTo(PASSED);
    assertThat(mock.testChar('x')).isEqualTo(PASSED);
    assertThat(mock.testChar('y')).isEqualTo(PASSED);
    assertThat(mock.testChar('z')).isEqualTo(PASSED);    
  }

  @Test
  public void testMoxyMockVerifyWithAnyShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testShort((short)0);
    mock.testShort((short)256);
    mock.testShort((short)65535);
        
    assertMock(() -> mock.testShort(anyShort())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testShort(anyShort())).thenReturn(PASSED);
    
    assertThat(mock.testShort((short)0)).isEqualTo(PASSED);
    assertThat(mock.testShort((short)1)).isEqualTo(PASSED);
    assertThat(mock.testShort((short)128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short)256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short)32768)).isEqualTo(PASSED);
    assertThat(mock.testShort((short)65535)).isEqualTo(PASSED);    
  }

  @Test
  public void testMoxyMockVerifyWithAnyIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);
        
    assertMock(() -> mock.testInt(anyInt())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testInt(anyInt())).thenReturn(PASSED);
    
    assertThat(mock.testInt(0)).isEqualTo(PASSED);
    assertThat(mock.testInt(1)).isEqualTo(PASSED);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE-1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);
        
    assertMock(() -> mock.testLong(anyLong())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testLong(anyLong())).thenReturn(PASSED);
    
    assertThat(mock.testLong(0)).isEqualTo(PASSED);
    assertThat(mock.testLong(1)).isEqualTo(PASSED);
    assertThat(mock.testLong(256)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE-1)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);
        
    assertMock(() -> mock.testFloat(anyFloat())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testFloat(anyFloat())).thenReturn(PASSED);
    
    assertThat(mock.testFloat(0.0f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(0.1f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MAX_VALUE-1)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);
        
    assertMock(() -> mock.testDouble(anyDouble())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testDouble(anyDouble())).thenReturn(PASSED);
    
    assertThat(mock.testDouble(0.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(0.1d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(256.7d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MAX_VALUE-1)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);
        
    assertMock(() -> mock.testBoolean(anyBool())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testBoolean(anyBool())).thenReturn(PASSED);
    
    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithAnyObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);
    
    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(any(), any())).wasCalled(3);
  }
  
  @Test
  public void testMoxyMockWhenWithAnyObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);
    
    when(() -> mock.sayHelloTo(any())).thenReturn(PASSED);
    
    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(eqByte((byte)128))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(eqByte(Byte.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(null);
    assertThat(mock.testByte((byte) 8)).isEqualTo(null);
    assertThat(mock.testByte((byte) 32)).isEqualTo(null);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(eqChar('b'))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(eqShort(Short.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(eqShort(Short.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(null);
    assertThat(mock.testShort((short) 1)).isEqualTo(null);
    assertThat(mock.testShort((short) 128)).isEqualTo(null);
    assertThat(mock.testShort((short) 256)).isEqualTo(null);
    assertThat(mock.testShort((short) 32767)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(eqInt(Integer.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(eqLong(Long.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(eqFloat(Float.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(eqFloat(Float.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(null);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(eqDouble(Double.MAX_VALUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithEqualsDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(eqDouble(Double.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(null);
    assertThat(mock.testDouble(0.1d)).isEqualTo(null);
    assertThat(mock.testDouble(256.7d)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(eqBool(Boolean.TRUE))).wasCalled(2);
  }

  @Test
  public void testMoxyMockWhenWithEqualsBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(eqBool(Boolean.TRUE))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithEqualsObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(eq("three"), eq("four"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(eq("one"), eq("four"))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithEqualsObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(eq("Steve"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(null);
  }
  
  @Test
  public void testMoxyMockVerifyWithNotEqualsByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 128);
    mock.testByte((byte) 255);

    assertMock(() -> mock.testByte(neqByte((byte)128))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(neqByte(Byte.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 1)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(PASSED);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(neqChar('b'))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(neqShort(Short.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(neqShort(Short.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 1)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(neqInt(Integer.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(neqLong(Long.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(neqFloat(Float.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(neqFloat(Float.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(0.1f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(neqDouble(Double.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(neqDouble(Double.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(0.1d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(256.7d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(neqBool(Boolean.TRUE))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(neqBool(Boolean.TRUE))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(null);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithNotEqualsObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("one", "two");
    mock.hasArgs("three", "four");
    mock.hasArgs("five", "six");

    assertMock(() -> mock.hasArgs(neq("three"), neq("four"))).wasCalledTwice();
    assertMock(() -> mock.hasArgs(neq("one"), neq("four"))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithNotEqualsObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(neq("Steve"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo(null);
    assertThat(mock.sayHelloTo("Bill")).isEqualTo(PASSED);
  }
  
  @Test
  public void testMoxyMockVerifyWithLessThanByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 64);
    mock.testByte((byte) 127);

    assertMock(() -> mock.testByte(ltByte((byte)64))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithLessThanByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(ltByte(Byte.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 1)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(PASSED);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(ltChar('b'))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithLessThanCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(ltChar('c'))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(PASSED);
    assertThat(mock.testChar('b')).isEqualTo(PASSED);
    assertThat(mock.testChar('c')).isEqualTo(null);
    assertThat(mock.testChar('x')).isEqualTo(null);
    assertThat(mock.testChar('y')).isEqualTo(null);
    assertThat(mock.testChar('z')).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(ltShort(Short.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithLessThanShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(ltShort(Short.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 1)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(ltInt(Integer.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithLessThanIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(ltInt(Integer.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(PASSED);
    assertThat(mock.testInt(1)).isEqualTo(PASSED);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(ltLong(Long.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithLessThanLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(ltLong(Long.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(PASSED);
    assertThat(mock.testLong(1)).isEqualTo(PASSED);
    assertThat(mock.testLong(256)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(ltFloat(Float.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithLessThanFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(ltFloat(Float.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(0.1f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(ltDouble(Double.MAX_VALUE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithLessThanDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(ltDouble(Double.MAX_VALUE))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(0.1d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(256.7d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(ltBool(Boolean.TRUE))).wasCalled(1);
  }

  @Test
  public void testMoxyMockWhenWithLessThanBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(ltBool(Boolean.TRUE))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(null);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithLessThanObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("aaaa", "bbbb");
    mock.hasArgs("cccc", "dddd");
    mock.hasArgs("eeee", "ffff");

    assertMock(() -> mock.hasArgs(lt("cccc"), lt("dddd"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(lt("aaaa"), lt("bbbb"))).wasNotCalled();
  }

  @Test
  public void testMoxyMockWhenWithLessThanObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(lt("BBBB"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("AAAA")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("BBBB")).isEqualTo(null);
    assertThat(mock.sayHelloTo("CCCC")).isEqualTo(null);
  }
  
  @Test
  public void testMoxyMockVerifyWithGreaterThanByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 64);
    mock.testByte((byte) 127);

    assertMock(() -> mock.testByte(gtByte((byte)64))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testByte(gtByte((byte)8))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(null);
    assertThat(mock.testByte((byte) 8)).isEqualTo(null);
    assertThat(mock.testByte((byte) 32)).isEqualTo(PASSED);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(gtChar('b'))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(gtChar('c'))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(null);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(null);
    assertThat(mock.testChar('x')).isEqualTo(PASSED);
    assertThat(mock.testChar('y')).isEqualTo(PASSED);
    assertThat(mock.testChar('z')).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 256);
    mock.testShort((short) 257);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(gtShort((short)256))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(gtShort((short)128))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(null);
    assertThat(mock.testShort((short) 1)).isEqualTo(null);
    assertThat(mock.testShort((short) 128)).isEqualTo(null);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 129)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(2);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(gtInt(1))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(gtInt(128))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(null);
    assertThat(mock.testInt(1)).isEqualTo(null);
    assertThat(mock.testInt(128)).isEqualTo(null);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(10);
    mock.testLong(1001);
    mock.testLong(Long.MAX_VALUE);

    assertMock(() -> mock.testLong(gtLong(1000))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(gtLong(256))).thenReturn(PASSED);

    assertThat(mock.testLong(0)).isEqualTo(null);
    assertThat(mock.testLong(1)).isEqualTo(null);
    assertThat(mock.testLong(256)).isEqualTo(null);
    assertThat(mock.testLong(257)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testLong(Long.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testLong(Long.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(gtFloat(1.0f))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(gtFloat(1.0f))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(null);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(gtDouble(1.0d))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(gtDouble(1.0d))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(null);
    assertThat(mock.testDouble(0.1d)).isEqualTo(null);
    assertThat(mock.testDouble(256.7d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testBoolean(Boolean.TRUE);
    mock.testBoolean(Boolean.FALSE);
    mock.testBoolean(Boolean.TRUE);

    assertMock(() -> mock.testBoolean(gtBool(Boolean.FALSE))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanBoolMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testBoolean(gtBool(Boolean.FALSE))).thenReturn(PASSED);

    assertThat(mock.testBoolean(Boolean.TRUE)).isEqualTo(PASSED);
    assertThat(mock.testBoolean(Boolean.FALSE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithGreaterThanObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("aaaa", "bbbb");
    mock.hasArgs("cccc", "dddd");
    mock.hasArgs("eeee", "ffff");

    assertMock(() -> mock.hasArgs(gt("cccc"), gt("dddd"))).wasCalledOnce();
    assertMock(() -> mock.hasArgs(gt("aaaa"), gt("bbbb"))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithGreaterThanObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(gt("BBBB"))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("AAAA")).isEqualTo(null);
    assertThat(mock.sayHelloTo("BBBB")).isEqualTo(null);
    assertThat(mock.sayHelloTo("CCCC")).isEqualTo(PASSED);
  }
  
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
  
  @Test
  public void testMoxyMockVerifyWithAndByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testByte((byte) 0);
    mock.testByte((byte) 63);
    mock.testByte((byte) 64);
    mock.testByte((byte) 126);
    mock.testByte((byte) 127);

    assertMock(() -> mock.testByte(andByte(gtByte((byte)63), ltByte((byte)127)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAndByteMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(andByte(gtByte((byte)1), ltByte(Byte.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testByte((byte) 0)).isEqualTo(null);
    assertThat(mock.testByte((byte) 1)).isEqualTo(null);
    assertThat(mock.testByte((byte) 8)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte) 32)).isEqualTo(PASSED);
    assertThat(mock.testByte(Byte.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithAndCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testChar('a');
    mock.testChar('b');
    mock.testChar('c');

    assertMock(() -> mock.testChar(andChar(gtChar('a'), ltChar('c')))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithAndCharMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testChar(andChar(gtChar('c'), ltChar('f')))).thenReturn(PASSED);

    assertThat(mock.testChar('a')).isEqualTo(null);
    assertThat(mock.testChar('b')).isEqualTo(null);
    assertThat(mock.testChar('c')).isEqualTo(null);
    assertThat(mock.testChar('d')).isEqualTo(PASSED);
    assertThat(mock.testChar('e')).isEqualTo(PASSED);
    assertThat(mock.testChar('f')).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithAndShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testShort((short) 0);
    mock.testShort((short) 1);
    mock.testShort((short) 256);
    mock.testShort(Short.MAX_VALUE);

    assertMock(() -> mock.testShort(andShort(gtShort((short)0), ltShort(Short.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAndShortMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testShort(andShort(gtShort((short)1), ltShort(Short.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testShort((short) 0)).isEqualTo(null);
    assertThat(mock.testShort((short) 1)).isEqualTo(null);
    assertThat(mock.testShort((short) 128)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 256)).isEqualTo(PASSED);
    assertThat(mock.testShort((short) 32767)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithAndIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testInt(0);
    mock.testInt(1);
    mock.testInt(2);
    mock.testInt(Integer.MAX_VALUE - 1);
    mock.testInt(Integer.MAX_VALUE);

    assertMock(() -> mock.testInt(andInt(gtInt(1), ltInt(Integer.MAX_VALUE)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAndIntMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testInt(andInt(gtInt(10), ltInt(Integer.MAX_VALUE)))).thenReturn(PASSED);

    assertThat(mock.testInt(0)).isEqualTo(null);
    assertThat(mock.testInt(1)).isEqualTo(null);
    assertThat(mock.testInt(256)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE - 1)).isEqualTo(PASSED);
    assertThat(mock.testInt(Integer.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithAndLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testLong(0);
    mock.testLong(1);
    mock.testLong(2);
    mock.testLong(99);
    mock.testLong(100);

    assertMock(() -> mock.testLong(andLong(gtLong(1), ltLong(100)))).wasCalledTwice();
  }

  @Test
  public void testMoxyMockWhenWithAndLongMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testLong(andLong(gtLong(10), ltLong(1000)))).thenReturn(PASSED);

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
  public void testMoxyMockVerifyWithAndFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testFloat(0.0f);
    mock.testFloat(1.4f);
    mock.testFloat(Float.MAX_VALUE);

    assertMock(() -> mock.testFloat(andFloat(gtFloat(1.0f), ltFloat(Float.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithAndFloatMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testFloat(andFloat(gtFloat(1.0f), ltFloat(500.0f)))).thenReturn(PASSED);

    assertThat(mock.testFloat(0.0f)).isEqualTo(null);
    assertThat(mock.testFloat(0.1f)).isEqualTo(null);
    assertThat(mock.testFloat(256.7f)).isEqualTo(PASSED);
    assertThat(mock.testFloat(Float.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testFloat(Float.MAX_VALUE)).isEqualTo(null);
  }

  @Test
  public void testMoxyMockVerifyWithAndDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    mock.testDouble(0.0d);
    mock.testDouble(1.4d);
    mock.testDouble(Double.MAX_VALUE);

    assertMock(() -> mock.testDouble(andDouble(gtDouble(1.0d), ltDouble(Double.MAX_VALUE)))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithAndDoubleMatcherWorks() {
    MatcherTestClass mock = mock(MatcherTestClass.class);

    when(() -> mock.testDouble(andDouble(gtDouble(5.0d), ltDouble(10.0d)))).thenReturn(PASSED);

    assertThat(mock.testDouble(0.0d)).isEqualTo(null);
    assertThat(mock.testDouble(6.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(9.0d)).isEqualTo(PASSED);
    assertThat(mock.testDouble(Double.MIN_VALUE)).isEqualTo(null);
    assertThat(mock.testDouble(Double.MAX_VALUE)).isEqualTo(null);
  }
  
  // TODO test Bool match when we've got matchers that make sense to AND for bools.

  @Test
  public void testMoxyMockVerifyWithAndObjectMatcherWorks() {
    MethodWithArguments mock = mock(MethodWithArguments.class);

    mock.hasArgs("AAAA", "DDDD");
    mock.hasArgs("BBBB", "DDDD");
    mock.hasArgs("BBBB", "No match");
    mock.hasArgs("CCCC", "DDDD");

    assertMock(() -> mock.hasArgs(and(gt("AAAA"), lt("CCCC")), eq("DDDD"))).wasCalledOnce();
  }

  @Test
  public void testMoxyMockWhenWithAndObjectMatcherWorks() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(and(gt("AAAA"), lt("DDDD")))).thenReturn(PASSED);

    assertThat(mock.sayHelloTo("AAAA")).isEqualTo(null);
    assertThat(mock.sayHelloTo("BBBB")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("CCCC")).isEqualTo(PASSED);
    assertThat(mock.sayHelloTo("DDDD")).isEqualTo(null);
  }
  
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
  
  @Test
  public void testMoxyMockWhenWithMatcherCanStubMultipleTimes() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

    when(() -> mock.sayHelloTo(eq("Steve"))).thenReturn("Hello, Steve");
    when(() -> mock.sayHelloTo(eq("Bill"))).thenReturn("Hello, Bill");

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Hello, Steve");
    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");
    assertThat(mock.sayHelloTo("Sam")).isEqualTo(null);
  }
  
  @Test
  public void testMoxyMockWhenWithMatcherFailsFastIfMixed() {
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

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
    MethodWithArgAndReturn mock = mock(MethodWithArgAndReturn.class);

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
    MatcherTestClass mock = mock(MatcherTestClass.class);
    
    when(() -> mock.testByte(anyByte())).thenReturn(PASSED);
    when(() -> mock.testBoolean(anyBool())).thenReturn(PASSED);
    
    assertThat(mock.testBoolean(true)).isEqualTo(PASSED);
    assertThat(mock.testByte((byte)10)).isEqualTo(PASSED);
    
    assertMock(() -> mock.testByte((byte)10)).wasCalledOnce();
    assertMock(() -> mock.testBoolean(true)).wasCalledOnce();
  }
}
