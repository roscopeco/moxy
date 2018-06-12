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

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MockGenerationException;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.model.ClassWithNoNullConstructor;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.FieldsClass;
import com.roscopeco.moxy.model.FinalClass;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleAbstractClass;
import com.roscopeco.moxy.model.SimpleClass;
import com.roscopeco.moxy.model.SimpleInterface;

/*
 * This is basically integration/e2e tests. It's actually the tests used
 * for high-level TDD.
 *
 * We run it with the rest of the tests though as it's quick enough.
 *
 * If it gets much bigger it may need to be split up though.
 */
public class TestMoxy {
  private static final String PASSED = "passed";
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testMoxyMockWithNullThrowsIllegalArgumentException() {
    assertThatThrownBy(() -> Moxy.mock(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Cannot mock null");
  }

  @Test
  public void testMoxyMockWithSimpleClassReturnsInstance() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleClass.class);
  }

  @Test
  public void testMoxyMockWithSimpleClassReturnsActualMock() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock.returnHello())
        .isNull();
  }

  @Test
  public void testMoxyMockWithNullEngineUsesDefaultEngine() {
    Moxy.setMoxyEngine(null);

    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleClass.class);
  }

  @Test
  public void testMoxyMockHasIsMockAnnotation() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(mock.getClass()).hasAnnotation(Mock.class);
  }

  @Test
  public void testMoxyIsMockWorks() {
    final SimpleClass mock = Moxy.mock(SimpleClass.class);

    assertThat(Moxy.isMock(mock.getClass())).isTrue();
    assertThat(Moxy.isMock(mock)).isTrue();
  }

  @Test
  public void testMoxyMockWithSimpleInterfaceReturnsInstance() {
    final SimpleInterface mock = Moxy.mock(SimpleInterface.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleInterface.class);
  }

  @Test
  public void testMoxyMockWithSimpleAbstractClassReturnsInstance() {
    final SimpleAbstractClass mock = Moxy.mock(SimpleAbstractClass.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(SimpleAbstractClass.class);
  }

  @Test
  public void testMoxyMockWithClassWithPrimitiveReturnsWorks() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(ClassWithPrimitiveReturns.class);
  }

  @Test
  public void testMoxyMockWithMethodArgumentsWorks() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(MethodWithArguments.class);
  }

  @Test
  public void testMoxyMockWithPrimitiveMethodArgumentsWorks() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    assertThat(mock)
        .isNotNull()
        .isInstanceOf(MethodWithPrimitiveArguments.class);

    assertThat(mock.hasArgs("", (byte)1, '2', (short)3, 4, 5L, 6.0f, 7.0d, true))
        .isEqualTo(0);
  }

  @Test
  public void testThatCanMockClassWithNoNullConstructor() {
    final ClassWithNoNullConstructor mock = Moxy.mock(ClassWithNoNullConstructor.class);

    Moxy.when(() -> mock.returnSomething()).thenReturn(PASSED);

    assertThat(mock.returnSomething()).isEqualTo(PASSED);
  }

  @Test
  public void testMoxyMockGeneratesPassthroughConstructors() throws Exception {
    Class<? extends ClassWithNoNullConstructor> mockClass =
        Moxy.getMoxyEngine().getMockClass(
            ClassWithNoNullConstructor.class,
            Collections.singleton(ClassWithNoNullConstructor.class.getMethod("getAnyInt")));

    ClassWithNoNullConstructor mock =
        mockClass.getConstructor(MoxyEngine.class, String.class)
            .newInstance(Moxy.getMoxyEngine(), PASSED);

    assertThat(mock.returnSomething()).isEqualTo(PASSED);

    mockClass =
        Moxy.getMoxyEngine().getMockClass(
            ClassWithNoNullConstructor.class,
            Collections.singleton(ClassWithNoNullConstructor.class.getMethod("returnSomething")));

    mock = mockClass.getConstructor(MoxyEngine.class, int.class)
        .newInstance(Moxy.getMoxyEngine(), 37);

    assertThat(mock.getAnyInt()).isEqualTo(37);
  }

  @Test
  public void testMoxyMockDoesntCopyFields() throws Exception {
    final FieldsClass mock = Moxy.mock(FieldsClass.class);

    // Ensure we've only got the support fields, and haven't copied any...
    assertThat(mock.getClass().getDeclaredFields())
        .hasSize(1)
        .doesNotContainAnyElementsOf(
            Lists.newArrayList(
                FieldsClass.class.getDeclaredField("intField"),
                FieldsClass.class.getDeclaredField("byteField"),
                FieldsClass.class.getDeclaredField("boolField"),
                FieldsClass.class.getDeclaredField("longField")
            ));
  }

  @Test
  public void testMoxyMockCanMockJavaLangClasses() {
    final Object objectMock = Moxy.mock(Object.class);

    assertThat(objectMock)
        .isNotNull()
        .isInstanceOf(Object.class);
  }

  @Test
  public void testMoxyMockWithFinalClassFailsFast() {
    assertThatThrownBy(() ->
        Moxy.mock(FinalClass.class)
    )
        .isInstanceOf(MockGenerationException.class)
        .hasMessage("Mocking of final classes is not yet supported");
  }

  @Test
  public void testMoxyMockThenStubThenResetWorksProperly() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final Exception ex = new Exception("No-one to say hello to");

    // Given I've stubbed some mocks...
    Moxy.when(() -> mock.sayHelloTo("Bill")).thenCallRealMethod();
    Moxy.when(() -> mock.sayHelloTo("Steve")).thenReturn("Oh hi, Steve");
    Moxy.when(() -> mock.sayHelloTo(null)).thenThrow(ex);

    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hello, Bill");
    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Oh hi, Steve");
    assertThatThrownBy(() ->
        mock.sayHelloTo(null)
    )
        .isSameAs(ex);

    // When I reset,
    Moxy.resetMock(mock);

    // Then the stubs are gone.
    assertThat(mock.sayHelloTo("Bill")).isNull();
    assertThat(mock.sayHelloTo("Steve")).isNull();
    assertThat(mock.sayHelloTo(null)).isNull();

    // And I can stub again.
    Moxy.when(() -> mock.sayHelloTo("Steve")).thenCallRealMethod();
    Moxy.when(() -> mock.sayHelloTo(null)).thenReturn("Oh hi, null");
    Moxy.when(() -> mock.sayHelloTo("Bill")).thenThrow(ex);

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Hello, Steve");
    assertThat(mock.sayHelloTo(null)).isEqualTo("Oh hi, null");
    assertThatThrownBy(() ->
        mock.sayHelloTo("Bill")
    )
        .isSameAs(ex);
  }
}
