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

package com.roscopeco.moxy;

import com.roscopeco.moxy.model.classmock.SimpleClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestMoxyMultithreaded {
    @Test
    void testMoxyMockWhenThenReturnDifferentThreads() throws InterruptedException {
        final SimpleClass mock = Moxy.mock(SimpleClass.class);
        Moxy.when(mock::returnHello).thenReturn("MARKER");

        final String[] r = new String[]{"NOTSET"};
        final Thread t = new Thread(() -> r[0] = mock.returnHello());
        t.start();
        t.join();

        assertThat(r[0]).isEqualTo("MARKER");
    }

    @Test
    void testMoxyMockWhenThenVerifyDifferentThreads() throws InterruptedException {
        final SimpleClass mock = Moxy.mock(SimpleClass.class);

        final Thread t1 = new Thread(mock::returnHello);
        final Thread t2 = new Thread(mock::returnHello);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        Moxy.assertMock(mock::returnHello).wasCalledTwice();
    }
}
