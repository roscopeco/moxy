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
package com.roscopeco.moxy.impl.asm.stubs;

import com.roscopeco.moxy.api.MoxyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/*
 * This is used as the value in the stubbed delegateTo
 * on the mocks.
 */
public final class StubDelegate implements Stub {
    private Method method;
    private final Object delegate;
    private final boolean retain;

    public StubDelegate(final Method method,
                        final Object delegate,
                        final boolean retain) {
        this.method = method;
        this.delegate = delegate;
        this.retain = retain;
    }

    @Override
    public StubType getType() {
        return StubType.DELEGATE;
    }

    @Override
    public boolean isRetained() {
        return this.retain;
    }

    @Override
    public Object getObject(final List<Object> actualArgs) {
        try {
            this.method.setAccessible(true);
            return this.method.invoke(this.delegate, actualArgs.toArray(new Object[0]));
        } catch (final InvocationTargetException e) {
            throw new MoxyException("Exception invoking delegate: " + e.getCause());
        } catch (final IllegalAccessException e) {
            throw new MoxyException("IllegalAccessException while calling delegate", e);
        }
    }
}