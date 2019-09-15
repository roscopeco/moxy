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
import com.roscopeco.moxy.annotations.model.TesterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestMockAnnotation {
    @BeforeEach
    void beforeEach() {
        MoxyAnnotations.initMocks(this);
    }

    @Mock
    private TesterClass publicMock;

    @Mock
    private TesterClass protectedMock;

    @Mock
    private TesterClass packageMock;

    @Mock
    private TesterClass privateMock;

    @Test
    void testSetsPublicMock() {
        assertThat(this.publicMock).isNotNull();
        assertThat(Moxy.isMock(this.publicMock)).isTrue();

        // Make sure it's actually a mock...
        assertThat(this.publicMock.test()).isNull();
    }

    @Test
    void testSetsProtectedMock() {
        assertThat(this.protectedMock).isNotNull();
        assertThat(Moxy.isMock(this.protectedMock)).isTrue();

        // Make sure it's actually a mock...
        assertThat(this.protectedMock.test()).isNull();
    }

    @Test
    void testSetsPackageMock() {
        assertThat(this.packageMock).isNotNull();
        assertThat(Moxy.isMock(this.packageMock)).isTrue();

        // Make sure it's actually a mock...
        assertThat(this.packageMock.test()).isNull();
    }

    @Test
    void testSetsPrivateMock() {
        assertThat(this.privateMock).isNotNull();
        assertThat(Moxy.isMock(this.privateMock)).isTrue();

        // Make sure it's actually a mock...
        assertThat(this.privateMock.test()).isNull();
    }
}
