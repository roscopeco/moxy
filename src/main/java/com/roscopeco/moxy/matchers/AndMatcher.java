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
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

class AndMatcher<T> implements MoxyMatcher<T> {
  // This is only used to determine how many matchers we should be popping
  final Object[] passedArgs;

  private final ArrayList<MoxyMatcher<? super T>> matchers = new ArrayList<>();

  @SafeVarargs
  AndMatcher(final T... objects) {
    this.passedArgs = objects;
  }

  public List<MoxyMatcher<? super T>> getMatchers() {
    return this.matchers;
  }

  @Override
  public boolean matches(final T arg) {
    return this.matchers.stream().allMatch(e -> e.matches(arg));
  }

  // NOTE inefficient way to reverse the matchers!
  // NOTE not strictly type-safe, but only used internally so it'll be fine...
  ///         ... as long as long as the stack stays consistent (!)
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void addToStack(final Deque<MoxyMatcher<?>> stack) {
    final int numMatchers = this.passedArgs.length;

    if (stack.size() < numMatchers) {
      throw new IllegalMatcherStateException("Not enough matchers for and(...) (Expected "
          + numMatchers + ")\n"
          + "Ensure you're passing another matcher to and(...)");
    } else {
      final ArrayDeque<MoxyMatcher<? super T>> deque = new ArrayDeque<>();
      for (int i = 0; i < numMatchers; i++) {
        deque.push((MoxyMatcher)stack.pop());
      }

      this.matchers.addAll(deque);
      stack.push(this);
    }
  }

  @Override
  public String toString() {
    if (this.matchers.size() < 3) {
      return "<and: "
          + this.matchers.stream().map(Object::toString).collect(Collectors.joining(", "))
          + ">";
    } else {
      return "<and: ...>";
    }
  }
}
