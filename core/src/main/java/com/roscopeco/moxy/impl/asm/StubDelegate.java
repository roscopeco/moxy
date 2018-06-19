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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.roscopeco.moxy.api.MoxyException;

/*
 * This is used as the value in the stubbed delegateTo
 * on the mocks.
 */
final class StubDelegate {
  final List<Object> args;
  Method method;
  final Object delegate;

  public StubDelegate(final List<Object> args,
                      final Method method,
                      final Object delegate) {
    super();
    this.args = args;
    this.method = method;
    this.delegate = delegate;
  }

  // TODO is this worth generating code for?
  Object invoke() {
    try {
      this.method.setAccessible(true);
      return this.method.invoke(this.delegate, this.args.toArray(new Object[this.args.size()]));
    } catch (final InvocationTargetException e) {
      throw new MoxyException("Exception invoking delegate: " + e.getCause());
    } catch (final IllegalAccessException e) {
      throw new MoxyException("IllegalAccessException while calling delegate", e);
    }
  }
}