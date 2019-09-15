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

package com.roscopeco.moxy.annotations;

import com.roscopeco.moxy.Moxy;

import java.lang.reflect.Field;

/**
 * Support methods to initialize mocks using annotations.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyAnnotations {
    private MoxyAnnotations() {
        throw new UnsupportedOperationException(
                "com.roscopeco.moxy.annotations.MoxyAnnotations is not designed for instantiation");
    }

    /**
     * <p>Initialize mock annotations on the given object.</p>
     *
     * <p>When called, this method will populate any fields of the
     * given object that are annotated with the {@link Mock}
     * annotation with appropriate mocks.</p>
     *
     * <p>Note that no contructors will be called on the mocks.</p>
     *
     * <p>This will usually be called from a <code>{@literal @}Before</code> or
     * <code>{@literal @}BeforeEach</code> method, e.g.:</p>
     *
     * <pre><code>
     * {@literal @}BeforeEach
     * public void setUp() {
     *   initMocks(this);
     * }
     * </code></pre>
     *
     * <p>Optionally (if using JUnit5) this can be
     * automated using the {@link com.roscopeco.moxy.annotations.junit5.InitMocks}
     * extension:</p>
     *
     * <pre><code>
     * {@literal @}ExtendWith(InitMocks.class)
     * public class SomeTest {
     *   // ...
     * }
     * </code></pre>
     *
     * @param test The test instance to initialize.
     * @since 1.0
     */
    public static void initMocks(final Object test) {
        for (final Field f : test.getClass().getDeclaredFields()) {
            Object mock = null;

            if (f.isAnnotationPresent(Mock.class)) {
                mock = Moxy.mock(f.getType());
            } else if (f.isAnnotationPresent(Spy.class)) {
                mock = Moxy.spy(f.getType());
            }

            if (mock != null) {
                f.setAccessible(true);

                try {
                    f.set(test, mock);
                } catch (final IllegalAccessException e) {
                    throw new InitializationException(e);
                }
            }
        }
    }
}