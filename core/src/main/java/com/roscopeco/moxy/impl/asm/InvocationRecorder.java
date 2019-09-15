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

import com.roscopeco.moxy.api.MoxyMatcher;
import com.roscopeco.moxy.matchers.InconsistentMatchersException;

import java.util.*;

/**
 * <p>Handles recording of invocations.</p>
 *
 * <p>This class records both <em>unmonitored</em> (i.e. standard) invocations and
 * <em>monitored</em> invocations (i.e. mock invocations within a <em>when</em> or
 * <em>verify</em> call).</p>
 *
 * <p>Standard invocations are recorded across all threads, with the recording being
 * done in a thread-safe manner. Monitored invocations are recorded on a thread-local
 * basis. This means that all stubbing and verifying must happen on the same thread
 * as the monitored call.</p>
 *
 * <p>For standard invocations, facilities are provided to find all invocations for
 * a given class, and to find <strong>all</strong> invocations, in the order they
 * were called.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class InvocationRecorder {
    private final ASMMoxyEngine engine;

    /*
     * This list keeps track of all 'standard' (i.e. unmonitored) invocations,
     * keyed by mock class, then by method for faster searching in whens and single-invocation
     * verifiers.
     */
    private final HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> invocationMap;

    /*
     * This list keeps track of all 'standard' (i.e. unmonitored) invocations.
     * These are invocations that do not occur in a when or assert.
     *
     * This is used by the multi-invocation verifiers.
     */
    private final ArrayList<Invocation> standardInvocationsOrderedList;

    /*
     * This stores the current invocation, and is valid *only* during invocation
     * of mocked methods. It is cleared before mocks return.
     */
    @SuppressWarnings("squid:S5164" /* False positive: We *do* call remove on this! */)
    private final ThreadLocal<Invocation> currentInvocationThreadLocal;

    /*
     * This stack keeps track of monitored invocations. Each time a monitored
     * invocation is started, a new 'frame' is pushed, and used at the end of
     * that invocation to retrieve the monitored invocations.
     *
     * It's a stack in case someone feels the need to nest when/asserts.
     */
    @SuppressWarnings("squid:S5164" /* False positive: We *do* call remove on this! */)
    private final ThreadLocal<ArrayDeque<ArrayList<Invocation>>> monitoredInvocationStackThreadLocal;

    InvocationRecorder(final ASMMoxyEngine engine) {
        this.engine = engine;
        this.invocationMap = new HashMap<>();
        this.standardInvocationsOrderedList = new ArrayList<>();
        this.currentInvocationThreadLocal = new ThreadLocal<>();
        this.monitoredInvocationStackThreadLocal = new ThreadLocal<>();
        this.monitoredInvocationStackThreadLocal.set(new ArrayDeque<>());
    }

    private ArrayDeque<ArrayList<Invocation>> ensureThreadLocalMonitoredInvocationStack() {
        ArrayDeque<ArrayList<Invocation>> stack;

        if ((stack = this.monitoredInvocationStackThreadLocal.get()) == null) {
            stack = new ArrayDeque<>();
            this.monitoredInvocationStackThreadLocal.set(stack);
        }

        return stack;
    }

    /*
     * This gets any matchers from the MatcherEngine's stack, and replaces
     * the arguments from the last invocation with them.
     *
     * This is called from the mock itself, so we're guaranteed to have
     * an invocation at this point.
     */
    private void replaceInvocationArgsWithMatchers(final Invocation invocation) {
        final ASMMoxyMatcherEngine mengine = this.engine.getMatcherEngine();
        final List<MoxyMatcher<?>> matchers = mengine.popMatchers();
        if (!matchers.isEmpty()) {
            final List<Object> lastArgs = invocation.getArgs();
            if (lastArgs.size() != matchers.size()) {
                throw new InconsistentMatchersException(lastArgs.size(), mengine.getMatcherStack());
            } else {
                for (int i = 0; i < lastArgs.size(); i++) {
                    lastArgs.set(i, matchers.get(i));
                }
            }
        }
    }

    // public as it's accessed from mocks (in different packages).
    public synchronized void recordInvocation(final Object receiver,
                                              final String methodName,
                                              final String methodDesc,
                                              final List<Object> args) {

        final List<Invocation> invocations = this.ensureInvocationList(
                this.ensureInvocationMap(this.ensureLocalClassMap(), receiver.getClass()),
                methodName, methodDesc);

        final Invocation invocation = new Invocation(receiver,
                methodName,
                methodDesc,
                args);

        // Fixup matchers
        this.replaceInvocationArgsWithMatchers(invocation);

        if (this.ensureThreadLocalMonitoredInvocationStack().isEmpty()) {
            // Not in a monitored invocation, add to standard map/list
            final List<Invocation> orderedInvocations =
                    this.ensureAllInvocationsOrderedList();

            // Add to list of invocations mapped by class (for faster lookup)
            invocations.add(invocation);

            // Add to ordered list (for in-order verification)
            orderedInvocations.add(invocation);
        } else {
            // In a monitored invocation, just add to list at top of stack.
            final List<Invocation> orderedInvocations =
                    this.monitoredInvocationStackThreadLocal.get().peek();

            orderedInvocations.add(invocation);
        }

        // Record current invocation on this thread.
        // Mocks rely on this to set their throws/returns,
        // so must always be set!
        this.currentInvocationThreadLocal.set(invocation);
    }

    /*
     * NOTE This does exactly what it says - deletes the invocation from the
     * lists, but not from the last invocation thread local. This means the engine
     * can still use the last invocation for the args.
     *
     * @see comments on {@link MoxyInvocationRecorder#unrecordLastInvocation}.
     */
    synchronized void unrecordLastInvocation() {
        final Invocation lastInvocation = this.getCurrentInvocation();

        if (lastInvocation != null) {
            final List<Invocation> invocations = this.ensureInvocationList(
                    this.ensureInvocationMap(this.ensureLocalClassMap(),
                            lastInvocation.getReceiver().getClass()),
                    lastInvocation.getMethodName(),
                    lastInvocation.getMethodDesc());

            invocations.remove(invocations.size() - 1);
        }
    }

    /*
     * Get invocations for the given class/method/desc combo, in order.
     *
     * Part of the contract of this method is that it returns a copy of the original list.
     */
    synchronized List<Invocation> getInvocationList(final Class<?> forClz, final String methodName, final String methodDesc) {
        return new ArrayList<>(this.ensureInvocationList(this.ensureInvocationMap(this.ensureLocalClassMap(), forClz), methodName, methodDesc));
    }

    /*
     * Get _all_ invocations, in order.
     *
     * Part of the contract of this method is that it returns a copy of the original list.
     */
    synchronized List<Invocation> getInvocationList() {
        return new ArrayList<>(this.ensureAllInvocationsOrderedList());
    }

    /*
     * Get the current invocation. Valid *only* during a mock invocation.
     */
    Invocation getCurrentInvocation() {
        return this.currentInvocationThreadLocal.get();
    }

    synchronized void reset() {
        this.standardInvocationsOrderedList.clear();
        this.invocationMap.clear();
    }

    void startMonitoredInvocation() {
        this.ensureThreadLocalMonitoredInvocationStack().push(new ArrayList<>());
    }

    List<Invocation> getCurrentMonitoredInvocations() {
        return this.ensureThreadLocalMonitoredInvocationStack().peek();
    }

    void endMonitoredInvocation() {
        if (this.ensureThreadLocalMonitoredInvocationStack().isEmpty()) {
            throw new IllegalStateException("[BUG] Attempt to end an unstarted monitored invocation (in recorder)");
        }

        this.currentInvocationThreadLocal.remove();
        this.monitoredInvocationStackThreadLocal.get().pop();

        if (this.monitoredInvocationStackThreadLocal.get().isEmpty()) {
            this.monitoredInvocationStackThreadLocal.remove();
        }
    }

    private HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> ensureLocalClassMap() {
        return this.invocationMap;
    }

    private List<Invocation> ensureAllInvocationsOrderedList() {
        return this.standardInvocationsOrderedList;
    }

    private LinkedHashMap<String, List<Invocation>> ensureInvocationMap(
            final HashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> classMap,
            final Class<?> forClz) {
        return classMap.computeIfAbsent(forClz, k -> new LinkedHashMap<>());
    }

    private List<Invocation> ensureInvocationList(
            final LinkedHashMap<String, List<Invocation>> invocationMap,
            final String forMethodName, final String forMethodDescriptor) {
        return invocationMap.computeIfAbsent(forMethodName + forMethodDescriptor,
                k -> new ArrayList<>());
    }
}
