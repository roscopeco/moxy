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

/**
 * <p>Implementations of this interface allow mocks to be verified after
 * use. They are returned by the
 * {@link MoxyEngine#assertMock(com.roscopeco.moxy.api.InvocationRunnable)}
 * method.</p>
 *
 * <p>Individual engines will usually provide their own implementation
 * of this interface, as verifying will require internal knowledge
 * of the engine's mocking strategy.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyVerifier {
    /**
     * Verify that the mock method was called <em>at least</em> once,
     * with the arguments specified in the call.
     * <p>
     * Note the subtle difference from {@link #wasCalled(int)}, which
     * expects an <em>exact number</em> of calls.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @return <code>this</code>.
     * @since 1.0
     */
    MoxyVerifier wasCalled();

    /**
     * Verify that the mock method was called <em>exactly</em> <code>times</code>,
     * with the arguments specified in the call.
     * <p>
     * Note the subtle difference from {@link #wasCalled()}, which
     * expects an <em>at least one</em> call.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @param times The number of expected invocations.
     * @return <code>this</code>.
     * @since 1.0
     */
    MoxyVerifier wasCalled(int times);

    /**
     * Verify that the mock method was not called.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier wasNotCalled();

    /**
     * Verify the mock method was called exactly once.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier wasCalledOnce();

    /**
     * Verify the mock method called exactly twice.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier wasCalledTwice();

    /**
     * Verify the mock method was called at least <code>times</code>.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @param times The number of expected invocations.
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier wasCalledAtLeast(int times);

    /**
     * Verify the mock method was called at most <code>times</code>.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @param times The number of expected invocations.
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier wasCalledAtMost(int times);

    /**
     * Verify the mocked method didn't throw an exception of type <code>throwable</code>.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @param throwable The exception type to check.
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier didntThrow(Class<? extends Throwable> throwable);

    /**
     * Verify the mocked method didn't throw the given <code>throwable</code> instance.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @param throwable The exception instance to check.
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier didntThrow(Throwable throwable);

    /**
     * Verify the mocked method didn't throw any exceptions.
     * <p>
     * Throws {@link AssertionFailedError} if the assertion fails.
     *
     * @return <code>this</code>
     * @since 1.0
     */
    MoxyVerifier didntThrowAnyException();
}
