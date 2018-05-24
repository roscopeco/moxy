package com.roscopeco.moxy;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.MatcherTestClass;

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
}
