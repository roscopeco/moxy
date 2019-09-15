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

import com.roscopeco.moxy.impl.asm.stubs.Stub;
import com.roscopeco.moxy.impl.asm.stubs.StubDoActions;
import com.roscopeco.moxy.impl.asm.stubs.StubInvocation;
import com.roscopeco.moxy.impl.asm.stubs.StubMethod;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Holds mock objects' "instance variables".
 *
 * This just keeps more of the code in Java, and means we only
 * have to generate one field/setter combo rather than five
 * as previously.
 */
public class ASMMockInstanceVars {
    static final class CachedDelegate {
        final Stub delegate;
        final List<Object> actualArgs;

        CachedDelegate(final Stub delegate, final List<Object> actualArgs) {
            this.delegate = delegate;
            this.actualArgs = actualArgs;
        }
    }

    private final ASMMoxyEngine engine;

    // Using Deque here for efficient add-at-front, so when
    // we stream we see the most recent stubbing...
    private final Map<StubMethod, Deque<StubInvocation>> stubsMap;

    private final Map<StubMethod, List<StubDoActions>> doActionsMap;

    // Cache for the stubdelegate - saves two lookups, argsmatch, etc.
    //
    // NOTE: This relies on generated code delegating immediately
    // if shouldDelegateForInvocation is true!
    //
    // It is subsequently cleared in runDelegateForInvocation...
    @SuppressWarnings("squid:S5164" /* This is remove()d in runDelegateForInvocation */)
    private final ThreadLocal<CachedDelegate> stubDelegateCache = new ThreadLocal<>();

    public ASMMockInstanceVars(final ASMMoxyEngine engine) {
        this.engine = engine;
        this.stubsMap = new HashMap<>();
        this.doActionsMap = new HashMap<>();
    }

    public ASMMoxyEngine getEngine() {
        return this.engine;
    }

    Map<StubMethod, Deque<StubInvocation>> getStubsMap() {
        return this.stubsMap;
    }

    Map<StubMethod, List<StubDoActions>> getDoActionsMap() {
        return this.doActionsMap;
    }

    ThreadLocal<CachedDelegate> getStubDelegateCache() {
        return this.stubDelegateCache;
    }
}