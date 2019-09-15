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

import com.roscopeco.moxy.model.SimpleConstructorClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestMoxyConstructMocksAndSpies {

    private static final String GOODBYE = "Goodbye";

    @BeforeEach
    void setUp() {
        Moxy.getMoxyEngine().reset();
    }

    @Test
    void testMoxyConstructMockWorksWithNullConstructor() {
        final SimpleConstructorClass mock = Moxy.constructMock(SimpleConstructorClass.class);

        Moxy.when(mock::returnString).thenCallRealMethod();

        assertThat(mock.returnString()).isEqualTo("Hello");
    }

    @Test
    void testMoxyConstructMockWorksWithOneArgConstructor() {
        final SimpleConstructorClass mock =
                Moxy.constructMock(SimpleConstructorClass.class, GOODBYE);

        Moxy.when(mock::returnString).thenCallRealMethod();

        assertThat(mock.returnString()).isEqualTo(GOODBYE);
    }

    @Test
    void testMoxyConstructMockWorksWithTwoArgConstructor() {
        final SimpleConstructorClass mock =
                Moxy.constructMock(SimpleConstructorClass.class, "Good", "bye");

        Moxy.when(mock::returnString).thenCallRealMethod();

        assertThat(mock.returnString()).isEqualTo(GOODBYE);
    }

    @Test
    void testMoxyConstructMockWorksWithPrimitiveArgConstructor() {
        final SimpleConstructorClass mock =
                Moxy.constructMock(SimpleConstructorClass.class, "Level", 42);

        Moxy.when(mock::returnString).thenCallRealMethod();

        assertThat(mock.returnString()).isEqualTo("Level42");
    }

    @Test
    void testMoxyConstructSpyWorksWithNullConstructor() {
        final SimpleConstructorClass mock = Moxy.constructSpy(SimpleConstructorClass.class);

        assertThat(mock.returnString()).isEqualTo("Hello");
    }

    @Test
    void testMoxyConstructSpyWorksWithOneArgConstructor() {
        final SimpleConstructorClass mock =
                Moxy.constructSpy(SimpleConstructorClass.class, GOODBYE);

        assertThat(mock.returnString()).isEqualTo(GOODBYE);
    }

    @Test
    void testMoxyConstructSpyWorksWithTwoArgConstructor() {
        final SimpleConstructorClass mock =
                Moxy.constructSpy(SimpleConstructorClass.class, "Good", "bye");

        assertThat(mock.returnString()).isEqualTo(GOODBYE);
    }

    @Test
    void testMoxyConstructSpyWorksWithPrimitiveArgConstructor() {
        final SimpleConstructorClass mock =
                Moxy.constructSpy(SimpleConstructorClass.class, "Level", 42);

        assertThat(mock.returnString()).isEqualTo("Level42");
    }
}
