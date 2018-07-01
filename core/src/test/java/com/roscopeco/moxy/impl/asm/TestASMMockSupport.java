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
  String returnMarker1 = "Marker1";
  String returnMarker2 = "Marker2";
  String returnMarker3 = "Marker3";

  Throwable throwMarker1 = new Throwable("Marker1");
  Throwable throwMarker2 = new Throwable("Marker2");
  Throwable throwMarker3 = new Throwable("Marker3");

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
  public void testPopReturnOrThrowForInvocation() {
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubReturn(this.returnMarker1, false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubThrow(this.throwMarker1, false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubSuper(false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubReturn(this.returnMarker3, false));

    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, true)).isEqualTo(this.returnMarker1);

    // pops return
    this.mock.__moxy_asm_popReturnOrThrowForInvocation(this.invoc);
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc, true)).isEqualTo(this.throwMarker1);

    // pops throw
    this.mock.__moxy_asm_popReturnOrThrowForInvocation(this.invoc);
    assertThat(this.mock.__moxy_asm_shouldCallSuperForInvocation(this.invoc)).isTrue();

    // doesn't pop (last one)
    this.mock.__moxy_asm_popReturnOrThrowForInvocation(this.invoc);
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, true)).isEqualTo(this.returnMarker3);

    this.mock.__moxy_asm_popReturnOrThrowForInvocation(this.invoc);
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, true)).isEqualTo(this.returnMarker3);

  }

  @Test
  public void testSetGetReturnForInvocation() {
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubReturn(this.returnMarker1, false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubReturn(this.returnMarker2, false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubReturn(this.returnMarker3, false));

    // was set
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, true)).isSameAs(this.returnMarker1);

    // was force retained
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, false)).isSameAs(this.returnMarker1);

    // was removed
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, false)).isSameAs(this.returnMarker2);

    // was removed
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, false)).isSameAs(this.returnMarker3);

    // was retained (last stubbing)
    assertThat(this.mock.__moxy_asm_getReturnableForInvocation(this.invoc, false)).isSameAs(this.returnMarker3);
  }

  @Test
  public void testSetGetThrowForInvocation() {
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubThrow(this.throwMarker1, false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubThrow(this.throwMarker2, false));
    this.mock.__moxy_asm_setStubbing(this.invoc, new StubThrow(this.throwMarker3, false));

    // was set
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc, true)).isSameAs(this.throwMarker1);

    // was force retained
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc, false)).isSameAs(this.throwMarker1);

    // was removed
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc, false)).isSameAs(this.throwMarker2);

    // was removed
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc, false)).isSameAs(this.throwMarker3);

    // was retained (last item)
    assertThat(this.mock.__moxy_asm_getThrowableForInvocation(this.invoc, false)).isSameAs(this.throwMarker3);
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
