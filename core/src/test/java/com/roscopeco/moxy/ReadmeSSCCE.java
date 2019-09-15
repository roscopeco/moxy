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

import org.junit.jupiter.api.Test;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;

class TestClass {
    public String sayHelloTo(final String who) {
        return "Hello, " + who;
    }

    String hasTwoArgs(final String arg1, final int arg2) {
        return "" + arg1 + arg2;
    }
}

final class HardToMockClass {
    static String staticSayHello(final String who) {
        return "Hello, " + who;
    }

    final String finalSayHello(final String who) {
        return "Goodbye, " + who;
    }
}

/**
 * SSCCE for the README.md.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
class ReadmeSSCCE {
    @Test
    void testClassMockVerifying() {
        mockClasses(HardToMockClass.class);

        when(() -> HardToMockClass.staticSayHello("Bill")).thenCallRealMethod();
        when(() -> HardToMockClass.staticSayHello("Steve")).thenReturn("Hi there, Steve!");

        assertThat(HardToMockClass.staticSayHello("Steve")).isEqualTo("Hi there, Steve!");
        assertThat(HardToMockClass.staticSayHello("Bill")).isEqualTo("Hello, Bill");

        final HardToMockClass mock = new HardToMockClass();

        when(() -> mock.finalSayHello("Jim")).thenAnswer(args -> "He's dead, Jim");

        assertThat(mock.finalSayHello("Jim")).isEqualTo("He's dead, Jim");

        assertMock(() -> HardToMockClass.staticSayHello(any())).wasCalledTwice();
        assertMock(() -> mock.finalSayHello(any())).wasCalledOnce();
    }

    @Test
    void testClassicMockVerifying() {
        final TestClass mock = mock(TestClass.class);

        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 1);

        assertMocks(() -> {
            mock.sayHelloTo("Steve");
            mock.hasTwoArgs("two", 2);
        })
                .wereNotCalled();

        assertMocks(() -> {
            mock.sayHelloTo("Bill");
            mock.hasTwoArgs("one", 1);
        })
                .wereAllCalledOnce()
                .inThatOrder();
    }
}
