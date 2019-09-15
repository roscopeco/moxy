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

import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.SimpleClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestMoxyThenDelegateTo {

    private static final String COMPATIBLE_HELLO = "Compatible hello";
    private static final String INCOMPATIBLE_HELLO = "Incompatible hello";

    static class SimpleClassCompatible {
        public String returnHello() {
            return COMPATIBLE_HELLO;
        }
    }

    static class SimpleClassIncompatible {
        public String returnHello(final String butHasArg) {
            return INCOMPATIBLE_HELLO;
        }
    }

    @Test
    void testThenDelegateToFailsWithNull() {
        final SimpleClass mock = Moxy.mock(SimpleClass.class);

        assertThatThrownBy(() ->
                Moxy.when(mock::returnHello).thenDelegateTo(null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot delegate to null");
    }

    @Test
    void testThenDelegateToWorksWithObjectReturn() {
        final SimpleClass delegate = new SimpleClass();

        final SimpleClass mock = Moxy.mock(SimpleClass.class);

        assertThat(delegate.returnHello()).isEqualTo("Hello");
        assertThat(mock.returnHello()).isNull();

        Moxy.assertMock(mock::returnHello).wasCalledOnce();

        Moxy.when(mock::returnHello).thenDelegateTo(delegate);

        assertThat(mock.returnHello()).isEqualTo("Hello");

        Moxy.assertMock(mock::returnHello).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithAnyCompatibleObject() {
        final SimpleClassCompatible delegate = new SimpleClassCompatible();

        final SimpleClass mock = Moxy.mock(SimpleClass.class);

        assertThat(delegate.returnHello()).isEqualTo(COMPATIBLE_HELLO);
        assertThat(mock.returnHello()).isNull();

        Moxy.assertMock(mock::returnHello).wasCalledOnce();

        Moxy.when(mock::returnHello).thenDelegateTo(delegate);

        assertThat(mock.returnHello()).isEqualTo(COMPATIBLE_HELLO);

        Moxy.assertMock(mock::returnHello).wasCalledTwice();
    }

    @Test
    void testThenDelegateToFailsFastWithIncompatibleObject() {
        final SimpleClassIncompatible delegate = new SimpleClassIncompatible();
        final SimpleClass mock = Moxy.mock(SimpleClass.class);

        assertThatThrownBy(() ->
                Moxy.when(mock::returnHello).thenDelegateTo(delegate)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot delegate invocation of java.lang.String returnHello() "
                        + "to object of class com.roscopeco.moxy.TestMoxyThenDelegateTo"
                        + "$SimpleClassIncompatible - no compatible method found");
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveByteReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnByte()).isEqualTo((byte) 10);
        assertThat(mock.returnByte()).isEqualTo((byte) 0);

        Moxy.assertMock(mock::returnByte).wasCalledOnce();

        Moxy.when(mock::returnByte).thenDelegateTo(delegate);

        assertThat(mock.returnByte()).isEqualTo((byte) 10);

        Moxy.assertMock(mock::returnByte).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveCharReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnChar()).isEqualTo('a');
        assertThat(mock.returnChar()).isEqualTo((char) 0);

        Moxy.assertMock(mock::returnChar).wasCalledOnce();

        Moxy.when(mock::returnChar).thenDelegateTo(delegate);

        assertThat(mock.returnChar()).isEqualTo('a');

        Moxy.assertMock(mock::returnChar).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveShortReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnShort()).isEqualTo((short) 10);
        assertThat(mock.returnShort()).isEqualTo((short) 0);

        Moxy.assertMock(mock::returnShort).wasCalledOnce();

        Moxy.when(mock::returnShort).thenDelegateTo(delegate);

        assertThat(mock.returnShort()).isEqualTo((short) 10);

        Moxy.assertMock(mock::returnShort).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveIntReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnInt()).isEqualTo(10);
        assertThat(mock.returnInt()).isEqualTo(0);

        Moxy.assertMock(mock::returnInt).wasCalledOnce();

        Moxy.when(mock::returnInt).thenDelegateTo(delegate);

        assertThat(mock.returnInt()).isEqualTo(10);

        Moxy.assertMock(mock::returnInt).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveLongReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnLong()).isEqualTo((long) 10);
        assertThat(mock.returnLong()).isEqualTo((long) 0);

        Moxy.assertMock(mock::returnLong).wasCalledOnce();

        Moxy.when(mock::returnLong).thenDelegateTo(delegate);

        assertThat(mock.returnLong()).isEqualTo((long) 10);

        Moxy.assertMock(mock::returnLong).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveFloatReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnFloat()).isEqualTo(10.0f);
        assertThat(mock.returnFloat()).isEqualTo(0.0f);

        Moxy.assertMock(mock::returnFloat).wasCalledOnce();

        Moxy.when(mock::returnFloat).thenDelegateTo(delegate);

        assertThat(mock.returnFloat()).isEqualTo(10.0f);

        Moxy.assertMock(mock::returnFloat).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveDoubleReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnDouble()).isEqualTo(10.0d);
        assertThat(mock.returnDouble()).isEqualTo(0.0d);

        Moxy.assertMock(mock::returnDouble).wasCalledOnce();

        Moxy.when(mock::returnDouble).thenDelegateTo(delegate);

        assertThat(mock.returnDouble()).isEqualTo(10.0d);

        Moxy.assertMock(mock::returnDouble).wasCalledTwice();
    }

    @Test
    void testThenDelegateToWorksWithPrimitiveBooleanReturns() {
        final ClassWithPrimitiveReturns delegate = new ClassWithPrimitiveReturns();

        final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

        assertThat(delegate.returnBoolean()).isEqualTo(true);
        assertThat(mock.returnBoolean()).isEqualTo(false);

        Moxy.assertMock(mock::returnBoolean).wasCalledOnce();

        Moxy.when(mock::returnBoolean).thenDelegateTo(delegate);

        assertThat(mock.returnBoolean()).isEqualTo(true);

        Moxy.assertMock(mock::returnBoolean).wasCalledTwice();
    }
}
