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
package com.roscopeco.moxy.matchers;

import java.util.ArrayDeque;
import java.util.Deque;

import com.roscopeco.moxy.api.MoxyException;

/**
 * <p>Thrown to indicate that the matcher stack was found to be
 * in an inconsistent state.</p>
 *
 * <p>This usually indicates that matchers have been mixed with
 * immediate value arguments in a stub or verify operation, or
 * that a matcher method has been called in an improper context
 * (i.e. not in a stub or verify operation).</p>
 *
 * <p>When this exception is thrown, the stack is also reset to
 * attempt to provide a consistent environment for future use.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class InconsistentMatchersException extends MoxyException {
  private static final long serialVersionUID = 1L;

  private final int expectedSize;
  private final ArrayDeque<MoxyMatcher<?>> stack;

  public InconsistentMatchersException(final int expectedSize,
                                       final Deque<MoxyMatcher<?>> stack) {
    super("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
        + "This limitation will (hopefully) be lifted in the future");

    this.expectedSize = expectedSize;
    this.stack = new ArrayDeque<>(stack);
  }

  int getExpectedSize() {
    return this.expectedSize;
  }

  int getActualSize() {
    return this.stack.size();
  }

  Deque<MoxyMatcher<?>> getStack() {
    return this.stack;
  }
}
