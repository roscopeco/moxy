package com.roscopeco.moxy.impl.asm;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.SimpleClass;

public class TestASMMockSupport extends AbstractImplTest {
  ASMMoxyEngine engine;
  ThreadLocalInvocationRecorder recorder;
  ASMMoxyMatcherEngine matcherEngine;
  ASMMockSupport mock;
  
  Invocation invoc;
  String returnMarker = "Marker";
  Throwable throwMarker = new Throwable("Marker");

  @BeforeEach
  public void setUp() {
    this.engine = new ASMMoxyEngine();
    this.mock = (ASMMockSupport)engine.mock(SimpleClass.class);
    this.invoc = new Invocation(mock,
                                "test",
                                "(Ljava/lang/String;Ljava/lang/String;)V",
                                Arrays.asList("arg1", "arg2"));
    
    this.recorder = engine.getRecorder();
    this.matcherEngine = engine.getASMMatcherEngine();
  }
  
  @Test
  public void testGetRecorder() {
    assertThat(mock.__moxy_asm_getRecorder()).isEqualTo(this.recorder);
  }
  
  @Test
  public void testSetGetReturnForInvocation() {    
    this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.returnMarker, true);
    assertThat(this.mock.__moxy_asm_getReturnForInvocation(this.invoc)).isSameAs(this.returnMarker);
  }
  
  @Test
  public void testSetGetThrowForInvocation() {
    this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.throwMarker, false);
    assertThat(this.mock.__moxy_asm_getThrowForInvocation(this.invoc)).isSameAs(this.throwMarker);
  }
  
  @Test
  public void testSetThrowFailsWithNonThrowable() {
    assertThatThrownBy(() ->
    this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.returnMarker, false)
)
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessage("Cannot throw non-Throwable class java.lang.String");
  }
  
  @Test
  public void testSetReturnAndThrowFails() {
    this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.returnMarker, true);
    
    assertThatThrownBy(() ->
        this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.throwMarker, false)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set throw for 'void test(java.lang.String, java.lang.String)' with arguments (\"arg1\", \"arg2\") as it has already been stubbed to return or call real method");
  }
  
  @Test
  public void testSetThrowAndReturnFails() {
    this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.throwMarker, false);
    
    assertThatThrownBy(() ->
        this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.returnMarker, true)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set return for 'void test(java.lang.String, java.lang.String)' with arguments (\"arg1\", \"arg2\") as it has already been stubbed to throw or call real method");
  }
  
  @Test
  public void testSetCallSuper() {
    assertThat(this.mock.__moxy_asm_shouldCallSuperForInvocation(this.invoc)).isFalse();
    
    this.mock.__moxy_asm_setShouldCallSuper(this.invoc, true);
    assertThat(this.mock.__moxy_asm_shouldCallSuperForInvocation(this.invoc)).isTrue();
  }
  
  @Test
  public void testSetReturnAndThrowFailFastIfSetCallSuper() {
    this.mock.__moxy_asm_setShouldCallSuper(this.invoc, true);

    assertThatThrownBy(() ->
        this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.returnMarker, true)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set return for 'void test(java.lang.String, java.lang.String)' with arguments (\"arg1\", \"arg2\") as it has already been stubbed to throw or call real method");
    
    assertThatThrownBy(() ->
        this.mock.__moxy_asm_setThrowOrReturn(this.invoc, this.throwMarker, false)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot set throw for 'void test(java.lang.String, java.lang.String)' with arguments (\"arg1\", \"arg2\") as it has already been stubbed to return or call real method");
  }
}
