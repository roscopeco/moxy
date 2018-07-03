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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.InvalidStubbingException;
import com.roscopeco.moxy.model.FinalClass;
import com.roscopeco.moxy.model.SimpleClass;
import com.roscopeco.moxy.model.classmock.ClassWithConstructorArgs;
import com.roscopeco.moxy.model.classmock.ClassWithStatic;
import com.roscopeco.moxy.model.classmock.SubclassWithConstructorArgs;

public class TestMoxyClassMock {
  public static class Delegate {
    public String returnHello() {
      return "Delegated";
    }
  }

  @BeforeEach
  public void resetMocks() {
    Moxy.resetAllClassMocks();
  }

  @Test
  public void testMoxyClassMockCanMockSimpleClass() throws Exception {
    Moxy.mockClasses(SimpleClass.class);

    final SimpleClass sc = new SimpleClass();

    assertThat(sc.returnHello()).isNull();
  }

  @Test
  public void testMoxyClassMockCanResetMockedClass() throws Exception {
    Moxy.mockClasses(SimpleClass.class);

    final SimpleClass sc = new SimpleClass();

    assertThat(sc.returnHello()).isNull();

    Moxy.resetClassMocks(SimpleClass.class);

    // Existing instances are reverted...
    assertThat(sc.returnHello()).isEqualTo("Hello");

    // ... as are new ones.
    assertThat(new SimpleClass().returnHello()).isEqualTo("Hello");
  }

  @Test
  public void testMoxyClassMockCanMockFinalClass() throws Exception {
    Moxy.mockClasses(FinalClass.class);

    final FinalClass sc = new FinalClass();

    assertThat(sc.returnHello()).isNull();
  }

  @Test
  public void testMoxyClassMockCanStubMockedClass() throws Exception {
    Moxy.mockClasses(FinalClass.class);

    final FinalClass sc = new FinalClass();

    assertThat(sc.returnHello()).isNull();

    Moxy.when(() -> sc.returnHello()).thenReturn("Goodbye");

    assertThat(sc.returnHello()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyClassMockCanStubWithAllStubs() throws Exception {
    Moxy.mockClasses(FinalClass.class);

    final FinalClass sc = new FinalClass();

    assertThat(sc.returnHello()).isNull();

    // thenReturn
    Moxy.when(() -> sc.returnHello()).thenReturn("Goodbye");
    assertThat(sc.returnHello()).isEqualTo("Goodbye");

    // thenCallRealMethod
    Moxy.when(() -> sc.returnHello()).thenCallRealMethod();
    assertThat(sc.returnHello()).isEqualTo("Hello");

    // thenDelegate
    Moxy.when(() -> sc.returnHello()).thenDelegateTo(new Delegate());
    assertThat(sc.returnHello()).isEqualTo("Delegated");

    // thenAnswer
    Moxy.when(() -> sc.returnHello()).thenAnswer(args -> "Answered");
    assertThat(sc.returnHello()).isEqualTo("Answered");

    // thenThrow
    final RuntimeException rte = new RuntimeException("THROWN");
    Moxy.when(() -> sc.returnHello()).thenThrow(rte);

    assertThatThrownBy(() -> sc.returnHello())
        .isSameAs(rte);
  }

  @Test
  public void testMoxyClassMockCanChainStubsAsNormal() throws Exception {
    Moxy.mockClasses(FinalClass.class);

    final FinalClass sc = new FinalClass();

    assertThat(sc.returnHello()).isNull();

    final RuntimeException rte = new RuntimeException("THROWN");

    Moxy.when(() -> sc.returnHello())
        .thenReturn("Goodbye")
        .thenCallRealMethod()
        .thenDelegateTo(new Delegate())
        .thenAnswer(args -> "Answered")
        .thenThrow(rte);

    assertThat(sc.returnHello()).isEqualTo("Goodbye");
    assertThat(sc.returnHello()).isEqualTo("Hello");
    assertThat(sc.returnHello()).isEqualTo("Delegated");
    assertThat(sc.returnHello()).isEqualTo("Answered");

    assertThatThrownBy(() -> sc.returnHello())
        .isSameAs(rte);
  }

  @Test
  public void testMoxyClassMockCanAssertMockedClass() throws Exception {
    Moxy.mockClasses(FinalClass.class);

    final FinalClass sc = new FinalClass();

    assertThat(sc.returnHello()).isNull();

    Moxy.assertMock(() -> sc.returnHello()).wasCalledOnce();

    Moxy.when(() -> sc.returnHello()).thenReturn("Goodbye");

    assertThat(sc.returnHello()).isEqualTo("Goodbye");

    Moxy.assertMock(() -> sc.returnHello()).wasCalledTwice();
  }

  @Test
  public void testMoxyClassMockCanMockWithConstructorArguments() {
    Moxy.mockClasses(ClassWithConstructorArgs.class);

    final ClassWithConstructorArgs mock = new ClassWithConstructorArgs("Hello Constructor");

    assertThat(mock.getStr()).isNull();

    Moxy.when(() -> mock.getStr()).thenCallRealMethod();

    assertThat(mock.getStr()).isEqualTo("Hello Constructor");
  }

  @Test
  public void testMoxyClassMockCanMockSubclassWithConstructorArguments() {
    Moxy.mockClasses(SubclassWithConstructorArgs.class);

    final SubclassWithConstructorArgs mock = new SubclassWithConstructorArgs("Hello Subclass Constructor");

    assertThat(mock.getStr()).isNull();

    Moxy.when(() -> mock.getStr()).thenCallRealMethod();

    assertThat(mock.getStr()).isEqualTo("Hello Subclass Constructor");
  }

  @Test
  public void testMoxyClassMockCanMockStaticMethods() {
    Moxy.mockClasses(ClassWithStatic.class);

    assertThat(ClassWithStatic.returnHello()).isNull();

    Moxy.when(() -> ClassWithStatic.returnHello()).thenReturn("Goodbye");

    assertThat(ClassWithStatic.returnHello()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyClassMockCanResetMockedClassStatics() throws Exception {
    Moxy.mockClasses(ClassWithStatic.class);

    assertThat(ClassWithStatic.returnHello()).isNull();

    Moxy.assertMock(() -> ClassWithStatic.returnHello()).wasCalledOnce();

    Moxy.when(() -> ClassWithStatic.returnHello()).thenReturn("Goodbye");

    assertThat(ClassWithStatic.returnHello()).isEqualTo("Goodbye");

    Moxy.resetClassMocks(ClassWithStatic.class);

    // Static are reverted
    assertThat(ClassWithStatic.returnHello()).isEqualTo("Hello");
  }

  @Test
  public void testMoxyClassMockCanAssertMockedStaticMethods() {
    Moxy.mockClasses(ClassWithStatic.class);

    assertThat(ClassWithStatic.returnHello()).isNull();

    Moxy.assertMock(() -> ClassWithStatic.returnHello()).wasCalledOnce();

    Moxy.when(() -> ClassWithStatic.returnHello()).thenReturn("Goodbye");

    assertThat(ClassWithStatic.returnHello()).isEqualTo("Goodbye");

    Moxy.assertMock(() -> ClassWithStatic.returnHello()).wasCalledTwice();
  }

  @Test
  public void testMoxyClassMockCanAssertConstructors() {
    Moxy.mockClasses(FinalClass.class);

    Moxy.assertMock(() -> new FinalClass()).wasNotCalled();

    @SuppressWarnings("unused")
    final FinalClass fc = new FinalClass();

    Moxy.assertMock(() -> new FinalClass()).wasCalledOnce();
  }

  @Test
  public void testMoxyClassMockCanStubConstructors() {
    final RuntimeException rte = new RuntimeException("MARKER");

    Moxy.mockClasses(FinalClass.class);

    Moxy.when(() -> new FinalClass()).thenThrow(rte);

    assertThatThrownBy(() -> new FinalClass()).isSameAs(rte);
  }

  @Test
  public void testMoxyClassMockCanStubConstructorsThrowsWithInvalidStubbing() {
    Moxy.mockClasses(FinalClass.class);

    Moxy.when(() -> new FinalClass()).thenCallRealMethod();

    assertThatThrownBy(() -> new FinalClass())
        .isInstanceOf(InvalidStubbingException.class)
        .hasMessage("Cannot call real method 'void <init>()' (constructors are not compatible with thenCallRealMethod)");
  }

  @Test
  public void testMoxyClassMockExistingInstanceAreAutomaticallyConvertedToSpies() {
    final FinalClass preExisting = new FinalClass();

    assertThat(preExisting.returnHello()).isEqualTo("Hello");

    assertThatThrownBy(() -> Moxy.when(() -> preExisting.returnHello()).thenReturn(""))
        .isInstanceOf(InvalidMockInvocationException.class)
        .hasMessage("No mock invocation found");

    Moxy.mockClasses(FinalClass.class);

    // pre-existing still behaves normally
    assertThat(preExisting.returnHello()).isEqualTo("Hello");

    // but new instances are normal mocks
    assertThat(new FinalClass().returnHello()).isNull();
  }
}
