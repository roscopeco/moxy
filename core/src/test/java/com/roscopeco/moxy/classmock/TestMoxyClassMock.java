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

package com.roscopeco.moxy.classmock;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.InvalidStubbingException;
import com.roscopeco.moxy.model.FinalClass;
import com.roscopeco.moxy.model.SimpleClass;
import com.roscopeco.moxy.model.classmock.ClassWithConstructorArgs;
import com.roscopeco.moxy.model.classmock.ClassWithStatic;
import com.roscopeco.moxy.model.classmock.SubclassWithConstructorArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestMoxyClassMock {

    private static final String HELLO = "Hello";
    private static final String GOODBYE = "Goodbye";
    private static final String ANSWERED = "Answered";
    private static final String DELEGATED = "Delegated";
    private static final String THROWN = "THROWN";

    public static class Delegate {
        public String returnHello() {
            return DELEGATED;
        }
    }

    @BeforeEach
    void resetMocks() {
        Moxy.resetAllClassMocks();
    }

    @Test
    void testMoxyClassMockCanMockSimpleClass() {
        Moxy.mockClasses(SimpleClass.class);

        final SimpleClass sc = new SimpleClass();

        assertThat(sc.returnHello()).isNull();
    }

    @Test
    void testMoxyClassMockCanResetMockedClass() {
        Moxy.mockClasses(SimpleClass.class);

        final SimpleClass sc = new SimpleClass();

        assertThat(sc.returnHello()).isNull();

        Moxy.resetClassMocks(SimpleClass.class);

        // Existing instances are reverted...
        assertThat(sc.returnHello()).isEqualTo(HELLO);

        // ... as are new ones.
        assertThat(new SimpleClass().returnHello()).isEqualTo(HELLO);
    }

    @Test
    void testMoxyClassMockCanMockFinalClass() {
        Moxy.mockClasses(FinalClass.class);

        final FinalClass sc = new FinalClass();

        assertThat(sc.returnHello()).isNull();
    }

    @Test
    void testMoxyClassMockCanStubMockedClass() {
        Moxy.mockClasses(FinalClass.class);

        final FinalClass sc = new FinalClass();

        assertThat(sc.returnHello()).isNull();

        Moxy.when(sc::returnHello).thenReturn(GOODBYE);

        assertThat(sc.returnHello()).isEqualTo(GOODBYE);
    }

    @Test
    void testMoxyClassMockCanStubWithAllStubs() {
        Moxy.mockClasses(FinalClass.class);

        final FinalClass sc = new FinalClass();

        assertThat(sc.returnHello()).isNull();

        // thenReturn
        Moxy.when(sc::returnHello).thenReturn(GOODBYE);
        assertThat(sc.returnHello()).isEqualTo(GOODBYE);

        // thenCallRealMethod
        Moxy.when(sc::returnHello).thenCallRealMethod();
        assertThat(sc.returnHello()).isEqualTo(HELLO);

        // thenDelegate
        Moxy.when(sc::returnHello).thenDelegateTo(new Delegate());
        assertThat(sc.returnHello()).isEqualTo(DELEGATED);

        // thenAnswer
        Moxy.when(sc::returnHello).thenAnswer(args -> ANSWERED);
        assertThat(sc.returnHello()).isEqualTo(ANSWERED);

        // thenThrow
        final RuntimeException rte = new RuntimeException(THROWN);
        Moxy.when(sc::returnHello).thenThrow(rte);

        assertThatThrownBy(sc::returnHello)
                .isSameAs(rte);
    }

    @Test
    void testMoxyClassMockCanChainStubsAsNormal() {
        Moxy.mockClasses(FinalClass.class);

        final FinalClass sc = new FinalClass();

        assertThat(sc.returnHello()).isNull();

        final RuntimeException rte = new RuntimeException(THROWN);

        Moxy.when(sc::returnHello)
                .thenReturn(GOODBYE)
                .thenCallRealMethod()
                .thenDelegateTo(new Delegate())
                .thenAnswer(args -> ANSWERED)
                .thenThrow(rte);

        assertThat(sc.returnHello()).isEqualTo(GOODBYE);
        assertThat(sc.returnHello()).isEqualTo(HELLO);
        assertThat(sc.returnHello()).isEqualTo(DELEGATED);
        assertThat(sc.returnHello()).isEqualTo(ANSWERED);

        assertThatThrownBy(sc::returnHello)
                .isSameAs(rte);
    }

    @Test
    void testMoxyClassMockCanAssertMockedClass() {
        Moxy.mockClasses(FinalClass.class);

        final FinalClass sc = new FinalClass();

        assertThat(sc.returnHello()).isNull();

        Moxy.assertMock(sc::returnHello).wasCalledOnce();

        Moxy.when(sc::returnHello).thenReturn(GOODBYE);

        assertThat(sc.returnHello()).isEqualTo(GOODBYE);

        Moxy.assertMock(sc::returnHello).wasCalledTwice();
    }

    @Test
    void testMoxyClassMockCanMockWithConstructorArguments() {
        Moxy.mockClasses(ClassWithConstructorArgs.class);

        final ClassWithConstructorArgs mock = new ClassWithConstructorArgs("Hello Constructor");

        assertThat(mock.getStr()).isNull();

        Moxy.when(mock::getStr).thenCallRealMethod();

        assertThat(mock.getStr()).isEqualTo("Hello Constructor");
    }

    @Test
    void testMoxyClassMockCanMockSubclassWithConstructorArguments() {
        Moxy.mockClasses(SubclassWithConstructorArgs.class);

        final SubclassWithConstructorArgs mock = new SubclassWithConstructorArgs("Hello Subclass Constructor");

        assertThat(mock.getStr()).isNull();

        Moxy.when(mock::getStr).thenCallRealMethod();

        assertThat(mock.getStr()).isEqualTo("Hello Subclass Constructor");
    }

    @Test
    void testMoxyClassMockCanMockStaticMethods() {
        Moxy.mockClasses(ClassWithStatic.class);

        assertThat(ClassWithStatic.returnHello()).isNull();

        Moxy.when(ClassWithStatic::returnHello).thenReturn(GOODBYE);

        assertThat(ClassWithStatic.returnHello()).isEqualTo(GOODBYE);
    }

    @Test
    void testMoxyClassMockCanResetMockedClassStatics() {
        Moxy.mockClasses(ClassWithStatic.class);

        assertThat(ClassWithStatic.returnHello()).isNull();

        Moxy.assertMock(ClassWithStatic::returnHello).wasCalledOnce();

        Moxy.when(ClassWithStatic::returnHello).thenReturn(GOODBYE);

        assertThat(ClassWithStatic.returnHello()).isEqualTo(GOODBYE);

        Moxy.resetClassMocks(ClassWithStatic.class);

        // Static are reverted
        assertThat(ClassWithStatic.returnHello()).isEqualTo(HELLO);
    }

    @Test
    void testMoxyClassMockCanAssertMockedStaticMethods() {
        Moxy.mockClasses(ClassWithStatic.class);

        assertThat(ClassWithStatic.returnHello()).isNull();

        Moxy.assertMock(ClassWithStatic::returnHello).wasCalledOnce();

        Moxy.when(ClassWithStatic::returnHello).thenReturn(GOODBYE);

        assertThat(ClassWithStatic.returnHello()).isEqualTo(GOODBYE);

        Moxy.assertMock(ClassWithStatic::returnHello).wasCalledTwice();
    }

    @Test
    void testMoxyClassMockCanAssertConstructors() {
        Moxy.mockClasses(FinalClass.class);

        Moxy.assertMock(FinalClass::new).wasNotCalled();

        @SuppressWarnings("unused") final FinalClass fc = new FinalClass();

        Moxy.assertMock(FinalClass::new).wasCalledOnce();
    }

    @Test
    void testMoxyClassMockCanStubConstructors() {
        final RuntimeException rte = new RuntimeException("MARKER");

        Moxy.mockClasses(FinalClass.class);

        Moxy.when(FinalClass::new).thenThrow(rte);

        assertThatThrownBy(FinalClass::new).isSameAs(rte);
    }

    @Test
    void testMoxyClassMockCanStubConstructorsThrowsWithInvalidStubbing() {
        Moxy.mockClasses(FinalClass.class);

        Moxy.when(FinalClass::new).thenCallRealMethod();

        assertThatThrownBy(FinalClass::new)
                .isInstanceOf(InvalidStubbingException.class)
                .hasMessage("Cannot call real method 'void <init>()' (constructors are not compatible with thenCallRealMethod)");
    }

    @Test
    void testMoxyClassMockExistingInstanceAreAutomaticallyConvertedToSpies() {
        final FinalClass preExisting = new FinalClass();

        assertThat(preExisting.returnHello()).isEqualTo(HELLO);

        assertThatThrownBy(() -> Moxy.when(preExisting::returnHello).thenReturn(""))
                .isInstanceOf(InvalidMockInvocationException.class)
                .hasMessage("No mock invocation found");

        Moxy.mockClasses(FinalClass.class);

        // pre-existing still behaves normally
        assertThat(preExisting.returnHello()).isEqualTo(HELLO);

        // but new instances are normal mocks
        assertThat(new FinalClass().returnHello()).isNull();
    }
}
