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

/**
 * Functional interface used to run monitored method invocations
 * (e.g. for <em>when(...)</em> and <em>assertMock(...)</em>) with
 * return type <code>T</code>.
 *
 * @param <T> Return type of the method to be invoked.
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
@FunctionalInterface
public interface InvocationSupplier<T> {
    /**
     * Run the invocation. The framework will swallow most types of exception.
     *
     * @return null (ignored).
     * @throws Exception Should never be thrown, just here to make the API more friendly.
     */
    @SuppressWarnings("squid:S00112" /* This has to be a generic exception type as user code could throw anything */)
    T get() throws Exception;
}
