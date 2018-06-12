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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.matchers.MatcherUsageError;
import com.roscopeco.moxy.matchers.MoxyMatcher;

class ASMMoxyMatcherEngine {
  final ASMMoxyEngine engine;

  ASMMoxyMatcherEngine(final ASMMoxyEngine engine) {
    this.engine = engine;
  }

  ThreadLocal<ArrayDeque<MoxyMatcher<?>>> matcherStack = new ThreadLocal<>();

  ASMMoxyEngine getASMMoxyEngine() {
    return this.engine;
  }

  private ArrayDeque<MoxyMatcher<?>> ensureMatcherStack() {
    ArrayDeque<MoxyMatcher<?>> stack = this.matcherStack.get();

    if (stack == null) {
      stack = new ArrayDeque<>();
      this.matcherStack.set(stack);
    }

    return stack;
  }

  ArrayDeque<MoxyMatcher<?>> getMatcherStack() {
    return this.ensureMatcherStack();
  }

  void verifyMatcherNotNull(final MoxyMatcher<?> matcher) {
    if (matcher == null) {
      throw new MoxyException("Null argument; see cause",
          new IllegalArgumentException("Cannot match to null"));
    }
  }

  void registerMatcher(final MoxyMatcher<?> matcher) {
    if (this.getASMMoxyEngine().isMockStubbingDisabledOnThisThread()) {
      this.verifyMatcherNotNull(matcher);
      matcher.addToStack(this.ensureMatcherStack());
    } else {
      throw new MatcherUsageError("Attempt to register matcher '"
                                  + matcher.toString()
                                  + "' outside when() or assertMock[s]() call");
    }
  }

  List<MoxyMatcher<?>> popMatchers() {
    final ArrayDeque<MoxyMatcher<?>> stack = this.ensureMatcherStack();
    if (stack.isEmpty()) {
      return null;
    } else {
      final ArrayList<MoxyMatcher<?>> result = new ArrayList<>();
      while (!stack.isEmpty()) {
        result.add(stack.removeLast());
      }
      return result;
    }
  }

  // suppress because we check manually
  @SuppressWarnings("unchecked")
  boolean argsMatch(final List<Object> actualArgs, final List<Object> storedArgs) {
    if (storedArgs.size() != actualArgs.size()) {
      return false;
    }

    boolean result = true;

    for (int i = 0; i < storedArgs.size(); i++) {
      final Object stored = storedArgs.get(i);
      final Object actual = actualArgs.get(i);

      if (stored instanceof MoxyMatcher) {
        final MoxyMatcher<Object> matcher = (MoxyMatcher<Object>)stored;
        if (!matcher.matches(actual)) {
          result = false;
        }
      } else {
        if (stored == null) {
          result = actual == null;
        } else {
          if (!stored.equals(actual)) {
            result = false;
          }
        }
      }
    }

    return result;
  }

  boolean clearMatcherStack() {
    final ArrayDeque<MoxyMatcher<?>> stack = this.matcherStack.get();
    if (stack != null && !stack.isEmpty()) {
      // clear stack as per contract of InconsistentMatchersException
      stack.clear();
      return true;
    }
    return false;

  }

  /*
   * Verifies the stack is empty. Called at entry and exit to the
   * framework (i.e. start and end of when() and assert() calls).
   *
   * If non-empty, throws InconsistentMatchersException.
   */
  void ensureStackConsistency() {
    if (this.clearMatcherStack()) {
      throw new InconsistentMatchersException(0, this.matcherStack.get());
    }
  }
}
