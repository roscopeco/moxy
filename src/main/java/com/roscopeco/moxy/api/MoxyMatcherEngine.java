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
package com.roscopeco.moxy.api;

import com.roscopeco.moxy.matchers.MoxyMatcher;

/**
 * <p>Implementations of this interface integrate the {@link MoxyMatcher}
 * API with the {@link com.roscopeco.moxy.api.MoxyEngine}.</p>
 *
 * <p>Implementations will usually be provided by the engine directly,
 * since they will require fairly tight integration.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyMatcherEngine {
  /**
   * <p>Register the given matcher with this <code>MoxyMatcherEngine</code>.</p>
   *
   * <p>This method triggers a two-step process, whereby the matcher engine
   * takes care of any housekeeping required by the current {@link MoxyEngine}
   * and then calls back to the {@link MoxyMatcher#addToStack(java.util.Deque)}
   * method, passing in the appropriate stack.</p>
   *
   * <p>See the documentation on {@link MoxyMatcher} for more information.</p>
   *
   * @param matcher The {@link MoxyMatcher} to register.
   */
  public void registerMatcher(MoxyMatcher<?> matcher);
}
