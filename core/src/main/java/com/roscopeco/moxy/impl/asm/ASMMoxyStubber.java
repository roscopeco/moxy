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

import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.impl.asm.stubs.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class ASMMoxyStubber<T> extends AbstractASMMoxyInvocationListProcessor implements MoxyStubber<T> {
    ASMMoxyStubber(final ASMMoxyEngine engine, final List<Invocation> invocations) {
        super(engine, Collections.unmodifiableList(invocations));

        // Reset when starting new stubbing...
        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();
        receiver.__moxy_asm_removePriorStubbing(invocation);
    }

    @Override
    public MoxyStubber<T> thenReturn(final T object) {
        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();

        receiver.__moxy_asm_setStubbing(invocation, new StubReturn(object, false));

        return this;
    }

    @Override
    public MoxyStubber<T> thenThrow(final Throwable throwable) {
        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();

        receiver.__moxy_asm_setStubbing(invocation, new StubThrow(throwable, false));

        return this;
    }

    @Override
    public MoxyStubber<T> thenCallRealMethod() {
        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();

        receiver.__moxy_asm_setStubbing(invocation, new StubSuper(false));

        return this;
    }

    @Override
    public MoxyStubber<T> thenAnswer(final Function<List<?>, T> provider) {
        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();

        receiver.__moxy_asm_setStubbing(invocation, new StubAnswer(provider, false));

        return this;
    }

    @Override
    public MoxyStubber<T> thenDelegateTo(final Object delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Cannot delegate to null");
        }

        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();
        final Class<?> delegateClass = delegate.getClass();

        final Method method = StubberHelpers.findCompatibleMethod(delegateClass,
                invocation.getMethodName(),
                invocation.getMethodDesc());

        if (method != null) {
            receiver.__moxy_asm_setStubbing(invocation, new StubDelegate(method, delegate, false));
        } else {
            throw new IllegalArgumentException(
                    "Cannot delegate invocation of "
                            + TypeStringUtils.javaMethodSignature(invocation)
                            + " to object of "
                            + delegate.getClass().toString()
                            + " - no compatible method found");
        }

        return this;
    }

    @Override
    public MoxyStubber<T> thenDo(final Consumer<List<?>> action) {
        final Invocation invocation = this.getLastMonitoredInvocation();
        final ASMMockSupport receiver = (ASMMockSupport) invocation.getReceiver();

        receiver.__moxy_asm_addDoAction(invocation, action);

        return this;
    }
}
