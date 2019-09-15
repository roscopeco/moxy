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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.roscopeco.moxy.Moxy.mock;
import static com.roscopeco.moxy.Moxy.when;
import static com.roscopeco.moxy.matchers.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestVerifierHelpers {
    private final Object seenOne = new Object();
    private final Object seenTwo = new Object();
    private final Object notSeen = new Object();

    private final Invocation i1_1 = new Invocation(this.seenOne, "m1", "()V", Collections.emptyList());
    private final Invocation i1_2 = new Invocation(this.seenOne, "m2", "()V", Collections.emptyList());
    private final Invocation i2_1 = new Invocation(this.seenTwo, "m1", "()V", Collections.emptyList());
    private final Invocation i2_2_1 = new Invocation(this.seenTwo, "m2", "(Ljava/lang/String;)V", Collections.singletonList("one"));
    private final Invocation i2_2_2 = new Invocation(this.seenTwo, "m2", "(Ljava/lang/String;)V", Collections.singletonList("two"));

    private final List<Invocation> invocations = Lists.newArrayList(this.i1_1, this.i1_2, this.i2_1, this.i2_2_1, this.i2_2_2);

    private final Invocation m1 = new Invocation(this.seenTwo, "m1", "()V", Collections.emptyList());

    private ASMMoxyMatcherEngine mockEngine;

    @BeforeEach
    void setUp() {
        this.mockEngine = mock(ASMMoxyMatcherEngine.class);
        when(() -> this.mockEngine.argsMatch(any(), any())).thenCallRealMethod();
    }

    @Test
    void testGetCallCountWithNullEngine() {
        assertThatThrownBy(() -> VerifierHelpers.getCallCount(null, this.m1, this.invocations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot match arguments with null engine");
    }

    @Test
    void testGetCallCountWithNullInvocation() {
        assertThatThrownBy(() -> VerifierHelpers.getCallCount(this.mockEngine, null, this.invocations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot match arguments with null invocation");
    }

    @Test
    void testGetCallCountWithNullList() {
        assertThatThrownBy(() -> VerifierHelpers.getCallCount(this.mockEngine, this.m1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot match arguments with null invocation list");
    }

    @Test
    void testGetCallCountWithDifferentReceivers() {
        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenOne, "m1", "()V", Collections.emptyList()),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenTwo, "m1", "()V", Collections.emptyList()),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.notSeen, "m1", "()V", Collections.emptyList()),
                this.invocations)).isEqualTo(0);
    }

    @Test
    void testGetCallCountWithDifferentMethod() {
        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenOne, "m1", "()V", Collections.emptyList()),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenOne, "m2", "()V", Collections.emptyList()),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenOne, "m3", "()V", Collections.emptyList()),
                this.invocations)).isEqualTo(0);
    }

    @Test
    void testGetCallCountWithDifferentDescriptors() {
        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenTwo, "m2", "(Ljava/lang/String;)V", Collections.singletonList("one")),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenTwo, "m2", "(Ljava/util/Object;)V", Collections.singletonList("one")),
                this.invocations)).isEqualTo(0);
    }

    @Test
    void testGetCallCountWithDifferentArgs() {
        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenTwo, "m2", "(Ljava/lang/String;)V", Collections.singletonList("one")),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenTwo, "m2", "(Ljava/lang/String;)V", Collections.singletonList("two")),
                this.invocations)).isEqualTo(1);

        assertThat(VerifierHelpers.getCallCount(this.mockEngine,
                new Invocation(this.seenTwo, "m2", "(Ljava/lang/String;)V", Collections.singletonList("three")),
                this.invocations)).isEqualTo(0);
    }

    // TODO Test with matchers
}
