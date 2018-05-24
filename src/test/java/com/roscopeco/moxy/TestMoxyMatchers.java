package com.roscopeco.moxy;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.model.MatcherTestClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

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
  public void testMoxyMockWhenWithAnyObjectMatcherFailsFastIfMixed() {
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
  public void testMoxyMockAssertWithAnyObjectMatcherFailsFastIfMixed() {
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
