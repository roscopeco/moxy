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

import java.lang.invoke.MethodHandles;

/**
 * <p>A {@link ClassDefinitionStrategy} implementation that uses a supplied <code>MethodHandles.Lookup</code>.
 * in order to define classes.</p>
 *
 * <p>This strategy ignores the <code>loader</code> parameter to the <code>defineClass</code> method.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class LookupClassDefinitionStrategy implements ClassDefinitionStrategy {
    private final MethodHandles.Lookup lookup;

    public LookupClassDefinitionStrategy(final MethodHandles.Lookup lookup) {
        this.lookup = lookup;
    }

    @Override
    public <T> Class<T> defineClass(final ClassLoader loader, final Class<T> originalClass, final byte[] code) {
        try {
            @SuppressWarnings("unchecked") final var clz = (Class<T>) MethodHandles.privateLookupIn(originalClass, this.lookup).defineClass(code);
            return clz;
        } catch (final IllegalAccessException e) {
            throw new MockGenerationException("Unable to define mock class", e);
        }
    }
}
