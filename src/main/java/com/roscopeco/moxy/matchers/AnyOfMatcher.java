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

import java.util.List;
import java.util.stream.Collectors;

class AnyOfMatcher<T> implements MoxyMatcher<T> {
  private final List<? extends T> objects;

  AnyOfMatcher(final List<? extends T> objects) {
    if (objects == null) {
      // fail fast
      throw new NullPointerException("Cannot match to null list - try an empty list (or eq(null)) instead");
    }

    this.objects = objects;
  }

  public List<? extends T> getObjects() {
    return this.objects;
  }

  @Override
  public boolean matches(final T arg) {
    return this.objects.contains(arg);
  }

  @Override
  public String toString() {
    if (this.objects.size() < 3) {
      return "<anyOf: "
          + this.objects.stream().map(Object::toString).collect(Collectors.joining(", "))
          + ">";
    } else {
      return "<anyOf: ...>";
    }
  }
}
