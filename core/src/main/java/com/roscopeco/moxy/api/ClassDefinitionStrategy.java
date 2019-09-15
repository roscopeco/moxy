/*
 * Moxy - Lean-and-mean mocking framework for Java with a fluent API.
 *
 * Copyright 2019 Ross Bamford
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
 * <p>A class definition strategy is used to actually define a mock class.</p>
 *
 * <p>The default implementation of this class uses reflective access to the
 * <code>defineClass(String, byte[], int, int, ProtectionDomain)</code> method
 * on <code>java.lang.ClassLoader</code>.</p>
 *
 * <p>There is an alternative implementation that uses a client-supplied <code>MethodHandles.Lookup</code>
 * to define the class. This will probably become the default under future Java versions.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @see LookupClassDefinitionStrategy
 * @since 1.0
 */
public interface ClassDefinitionStrategy {
    /**
     * Define a class in the manner supported by this strategy.
     *
     * @param targetLoader  Desired Target ClassLoader. Not all implementations must support this.
     * @param originalClass The original class.
     * @param mockCode      The bytecode for the mock.
     * @param <T>           The type of the class being mocked.
     * @return A newly-defined class.
     */
    <T> Class<T> defineClass(ClassLoader targetLoader, Class<T> originalClass, byte[] mockCode);
}
