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

import com.roscopeco.moxy.api.InvalidMockInvocationException;

class AbstractASMMoxyVerifier {
  protected final ASMMoxyEngine engine;
  protected final List<Invocation> invocations;

  public AbstractASMMoxyVerifier(final ASMMoxyEngine engine,
                                 final List<Invocation> monitoredInvocations) {
    this.engine = engine;
    this.invocations = monitoredInvocations;

    if (this.engine == null) {
      throw new IllegalArgumentException("Cannot construct with null engine");
    }

    if (this.invocations == null ||
        this.invocations.isEmpty() ||
        this.invocations.stream().anyMatch(i -> i.getReceiver() == null)) {
      throw new InvalidMockInvocationException("No mock invocation found");
    }
  }

  protected ASMMoxyEngine getEngine() {
    return this.engine;
  }

  protected ThreadLocalInvocationRecorder getRecorder() {
    return this.engine.getRecorder();
  }

  protected Invocation getLastMonitoredInvocation() {
    return this.invocations.get(this.invocations.size() - 1);
  }

  protected List<Invocation> getMonitoredInvocations() {
    return this.invocations;
  }
}