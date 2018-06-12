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

import java.util.List;
import java.util.function.Consumer;

import com.roscopeco.moxy.api.MoxyVoidStubber;

class ASMMoxyVoidStubber extends AbstractASMMoxyVerifier implements MoxyVoidStubber {
  public ASMMoxyVoidStubber(final ASMMoxyEngine engine, final List<Invocation> invocations) {
    super(engine, invocations);
  }

  @Override
  public void thenThrow(final Throwable throwable) {
    final Invocation invocation = this.getLastMonitoredInvocation();
    final ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();

    receiver.__moxy_asm_setThrowOrReturn(invocation, throwable, false);
  }

  @Override
  public void thenCallRealMethod() {
    final Invocation invocation = this.getLastMonitoredInvocation();
    final ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();

    receiver.__moxy_asm_setShouldCallSuper(invocation, true);
  }

  @Override
  public MoxyVoidStubber thenDo(final Consumer<List<? extends Object>> action) {
    final Invocation invocation = this.getLastMonitoredInvocation();
    final ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();

    receiver.__moxy_asm_addDoAction(invocation, action);

    return this;
  }
}
