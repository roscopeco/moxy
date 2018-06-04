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

import java.util.Deque;

class NotMatcher<T> implements MoxyMatcher<T> {
  private MoxyMatcher<? super T> matcher;

  public MoxyMatcher<? super T> getMatcher() {
    return this.matcher;
  }

  @Override
  public boolean matches(final T arg) {
    return !this.matcher.matches(arg);
  }

  // NOTE not strictly type-safe, but only used internally so it'll be fine...
  ///         ... as long as long as the stack stays consistent (!)
  @SuppressWarnings("unchecked")
  @Override
  public void addToStack(final Deque<MoxyMatcher<?>> stack) {
    if (stack.isEmpty()) {
      throw new IllegalMatcherStateException("Not enough matchers for not(...) (Expected 1)\n"
                                           + "Ensure you're passing another matcher to not(...)");
    } else {
      this.matcher = (MoxyMatcher<? super T>)stack.pop();
      stack.push(this);
    }
  }

  @Override
  public String toString() {
    return "<not: " + this.matcher.toString() + ">";
  }
}
