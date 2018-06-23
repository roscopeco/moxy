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

import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

/**
 * <p>Implementations of this interface allow multiple mocks invocations to be
 * verified after use. They are returned by the
 * {@link MoxyEngine#assertMocks(com.roscopeco.moxy.api.InvocationRunnable)}
 * method. For example:</p>
 *
 * <pre><code>
 * assertMocks(() -&gt; {
 *   mock1.method1(any());
 *   mock1.method2(anyInt(), eq("Hello"));
 *
 *   mock2.method1(anyBoolean());
 * }).wereAllCalled();
 * </code></pre>
 *
 * <p>Individual engines will usually provide their own implementation
 * of this interface, as verifying will require internal knowledge
 * of the engine's mocking strategy.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyMultiVerifier {
  /**
   * <p>Verify that none of the mock methods were called at all.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation was
   * matched, or {@link MultipleFailuresError} if multiple calls were
   * matched. In the latter case, the individual calls that matched
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   */
  public void wereNotCalled();

  /**
   * <p>Verify that all the mock methods were called <em>at least</em> once,
   * with the arguments specified in the call.</p>
   *
   * <p>Note the subtle difference from {@link #wereAllCalledExactly(int)}, which
   * expects an <em>exact number</em> of calls.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @return <code>this</code>.
   * @since 1.0
   */
  public MoxyMultiVerifier wereAllCalled();

  /**
   * <p>Verify that all the mock methods were called exactly <code>expectedTimes</code>
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @param expectedTimes The expected number of invocations for each method.
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledExactly(int expectedTimes);

  /**
   * <p>Verify that all the mock methods were called exactly once
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledOnce();

  /**
   * <p>Verify that all the mock methods were called exactly twice
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledTwice();

  /**
   * <p>Verify that all the mock methods were called at least <code>expectedTimes</code>
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @param expectedTimes The expected number of invocations.
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledAtLeast(int expectedTimes);

  /**
   * <p>Verify that all the mock methods were called at least twice
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledAtLeastTwice();

  /**
   * <p>Verify that all the mock methods were called at most <code>expectedTimes</code>
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @param expectedTimes The expected number of invocations.
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledAtMost(int expectedTimes);

  /**
   * <p>Verify that all the mock methods were called at most twice
   * with the arguments specified in the call.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   * @return <code>this</code>.
   */
  public MoxyMultiVerifier wereAllCalledAtMostTwice();

  /**
   * <p>Verifies that the mock methods were called (with matching
   * arguments) in the order they were called within the lambda
   * passed to assertMocks.</p>
   *
   * <p>This method is tolerant of other mock invocations in between
   * the matched calls.</p>
   *
   * <p>Note that this <strong>does not</strong> directly verify
   * that all methods were called (although it does have that side-effect).
   * You may find it more readable (and will give better failure messages)
   * to chain this with one of the {@link #wereAllCalled()} family
   * of methods.</p>
   *
   * <p>Throws {@link AssertionFailedError} if the methods were not called
   * in the order given.</p>
   *
   * @since 1.0
   */
  public void inThatOrder();

  /**
   * <p>Verifies that the mock methods were called (with matching
   * arguments) in the order they were called within the lambda
   * passed to assertMocks.</p>
   *
   * <p>This method is not tolerant of other mock invocations in between
   * the matched calls - it requires them to have been exclusively
   * called, in the given order.</p>
   *
   * <p>Note that this <strong>does not</strong> directly verify
   * that all methods were called (although it does have that side-effect).
   * You may find it more readable (and will give better failure messages)
   * to chain this with one of the {@link #wereAllCalled()} family
   * of methods.</p>
   *
   * Throws {@link AssertionFailedError} if the methods were not called
   * in the order given.
   *
   * @since 1.0
   */
  public void exclusivelyInThatOrder();

  /**
   * <p>Verifies that the mock methods were called (with matching
   * arguments) in any order. In the default implementation, this is
   * a synonym for {@link #wereAllCalled()}. </p>
   *
   * <p>This method is tolerant of other mock invocations in between
   * the matched calls.</p>
   *
   * <p>Throws {@link AssertionFailedError} if a single invocation wasn't
   * matched, or {@link MultipleFailuresError} if multiple calls weren't
   * matched. In the latter case, the individual calls that didn't match
   * will each have a nested {@link AssertionFailedError}.</p>
   *
   * @since 1.0
   */
  public void inAnyOrder();
}
