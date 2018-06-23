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
package com.roscopeco.moxy.impl.asm;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.impl.asm.stubs.StubDelegate;
import com.roscopeco.moxy.impl.asm.stubs.StubReturn;
import com.roscopeco.moxy.impl.asm.stubs.StubSuper;
import com.roscopeco.moxy.impl.asm.stubs.StubThrow;
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
    this.mock = (ASMMockSupport)this.engine.mock(SimpleClass.class);
    this.invoc = new Invocation(this.mock,
                                "test",
                                "(Ljava/lang/String;Ljava/lang/String;)V",
                                Arrays.asList("arg1", "arg2"));

    this.recorder = this.engine.getRecorder();
    this.matcherEngine = this.engine.getMatcherEngine();
  }

  @Test
  public void testGetRecorder() {
    assertThat(this.mock.__moxy_asm_getRecorder()).isEqualTo(this.recorder);
  }

  @Test
  public void testSetGetReturnForInvocation() {
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubReturn(this.returnMarker, false));
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc)).isSameAs(this.returnMarker);
  }

  @Test
  public void testSetGetThrowForInvocation() {
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubThrow(this.throwMarker, false));
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc)).isSameAs(this.throwMarker);
  }

  @Test
  public void testSetCallSuper() {
    assertThat(this.mock.__moxy_asm_shouldCallSuperForInvocation(this.invoc)).isFalse();

    this.mock.__moxy_asm_setStubbing(this.invoc, new StubSuper(false));
    assertThat(this.mock.__moxy_asm_shouldCallSuperForInvocation(this.invoc)).isTrue();
  }

  @Test
  public void testSetDelegate() throws ReflectiveOperationException {
    assertThat(this.mock.__moxy_asm_shouldDelegateForInvocation(this.invoc)).isFalse();

    this.mock.__moxy_asm_setStubbing(this.invoc, new StubDelegate(SimpleClass.class.getMethod("returnHello"), new SimpleClass(), false));
    assertThat(this.mock.__moxy_asm_shouldDelegateForInvocation(this.invoc)).isTrue();
  }

  @Test
  public void testIsMockBehaviourDisabledOnThisThread() {
    assertThat(this.mock.__moxy_asm_isMockBehaviourDisabledOnThisThread()).isFalse();

    this.engine.startMonitoredInvocation();
    assertThat(this.mock.__moxy_asm_isMockBehaviourDisabledOnThisThread()).isTrue();

    this.engine.endMonitoredInvocation();
    assertThat(this.mock.__moxy_asm_isMockBehaviourDisabledOnThisThread()).isFalse();
  }

  // TODO test set/run doActions
}
