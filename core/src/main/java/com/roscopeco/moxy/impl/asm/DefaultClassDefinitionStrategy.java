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
package com.roscopeco.moxy.impl.asm;

import com.roscopeco.moxy.api.ClassDefinitionStrategy;
import com.roscopeco.moxy.api.MockGenerationException;
import com.roscopeco.moxy.api.MoxyException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

/**
 * This is the default {@link ClassDefinitionStrategy} used by the ASM implementation.
 *
 * @since 1.0
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class DefaultClassDefinitionStrategy implements ClassDefinitionStrategy {
    private final MethodHandle defineClass;

    public DefaultClassDefinitionStrategy() {
        try {
            final var method = ClassLoader.class.getDeclaredMethod(
                    "defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
            method.setAccessible(true);

            this.defineClass = MethodHandles.lookup().unreflect(method);
        } catch (final NoSuchMethodException | IllegalAccessException e) {
            throw new MoxyException("Unable to initialize default strategy; java.lang.ClassLoader not compatible", e);
        }
    }

    @Override
    public <T> Class<T> defineClass(final ClassLoader loader, final Class<T> originalClass, final byte[] code) {
        if (loader == null) {
            throw new IllegalArgumentException(
                    "Implicit definition in the system classloader is unsupported.\n"
                            + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
                            + "If you're sure this is what you want to do, pass system loader explicitly (rather than null)");
        }

        try {
            @SuppressWarnings("unchecked") final var clz = (Class<T>) this.defineClass.invoke(loader, null, code, 0, code.length, null);
            return clz;
        } catch (final IllegalAccessException e) {
            throw new MockGenerationException("Unable to define mock class", e);
        } catch (final InvocationTargetException e) {
            throw new MockGenerationException("Supplied class loader is not compatible", e);
        } catch (final Throwable t) {
            throw new MockGenerationException("Exception during class definition", t);
        }
    }
}
