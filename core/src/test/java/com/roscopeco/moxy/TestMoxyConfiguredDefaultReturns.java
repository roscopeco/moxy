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
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.ClassWithDefaultConfiguredReturnTypes;

public class TestMoxyConfiguredDefaultReturns {
  @Test
  public void testMoxyMockDefaultConfiguredReturnTypeOptional() {
    final ClassWithDefaultConfiguredReturnTypes mock = Moxy.mock(ClassWithDefaultConfiguredReturnTypes.class);

    // returns empty optional by default
    assertThat(mock.returnOptionalString())
        .isNotNull()
        .isNotPresent()
        .isEmpty();

    // and can still be stubbed
    Moxy.when(() -> mock.returnOptionalString()).thenReturn(Optional.of("Hello there!"));

    assertThat(mock.returnOptionalString())
        .isNotNull()
        .isPresent()
        .contains("Hello there!");

    // etc.
    Moxy.when(() -> mock.returnOptionalString()).thenCallRealMethod();

    assertThat(mock.returnOptionalString())
        .isNotNull()
        .isPresent()
        .contains("Hello, World");
  }

  @Test
  public void testMoxyMockDefaultConfiguredReturnTypeList() {
    final ClassWithDefaultConfiguredReturnTypes mock = Moxy.mock(ClassWithDefaultConfiguredReturnTypes.class);

    // returns empty list by default
    assertThat(mock.returnListOfString())
        .isNotNull()
        .isEmpty();

    // and can still be stubbed
    Moxy.when(() -> mock.returnListOfString()).thenReturn(Collections.singletonList("Hello there!"));

    assertThat(mock.returnListOfString())
        .isNotNull()
        .isNotEmpty()
        .contains("Hello there!");

    // etc.
    Moxy.when(() -> mock.returnListOfString()).thenCallRealMethod();

    assertThat(mock.returnListOfString())
        .isNotNull()
        .isNotEmpty()
        .contains("Hello, World");
  }

  @Test
  public void testMoxyMockDefaultConfiguredReturnTypeMap() {
    final ClassWithDefaultConfiguredReturnTypes mock = Moxy.mock(ClassWithDefaultConfiguredReturnTypes.class);

    // returns empty map by default
    assertThat(mock.returnMapStringToString())
        .isNotNull()
        .isEmpty();

    // and can still be stubbed
    Moxy.when(() -> mock.returnMapStringToString()).thenReturn(Collections.singletonMap("Hello", "There!"));

    assertThat(mock.returnMapStringToString())
        .isNotNull()
        .isNotEmpty()
        .containsOnlyKeys("Hello")
        .containsValue("There!");

    // etc.
    Moxy.when(() -> mock.returnMapStringToString()).thenCallRealMethod();

    assertThat(mock.returnMapStringToString())
        .isNotNull()
        .isNotEmpty()
        .containsOnlyKeys("Hello")
        .containsValue("World");
  }

  @Test
  public void testMoxyMockDefaultConfiguredReturnTypeSet() {
    final ClassWithDefaultConfiguredReturnTypes mock = Moxy.mock(ClassWithDefaultConfiguredReturnTypes.class);

    // returns empty set by default
    assertThat(mock.returnSetOfString())
        .isNotNull()
        .isEmpty();

    // and can still be stubbed
    Moxy.when(() -> mock.returnSetOfString()).thenReturn(Collections.singleton("Hello There!"));

    assertThat(mock.returnSetOfString())
        .isNotNull()
        .isNotEmpty()
        .contains("Hello There!");

    // etc.
    Moxy.when(() -> mock.returnSetOfString()).thenCallRealMethod();

    assertThat(mock.returnSetOfString())
        .isNotNull()
        .isNotEmpty()
        .contains("Hello, World");
  }

  @Test
  public void testMoxyMockDefaultConfiguredReturnTypeReconfiguration() {
    Moxy.getMoxyEngine().registerDefaultReturnForType(Optional.class.getName(), () -> Optional.of("Goodbye"));

    final ClassWithDefaultConfiguredReturnTypes mock = Moxy.mock(ClassWithDefaultConfiguredReturnTypes.class);

    // returns configured default
    assertThat(mock.returnOptionalString())
        .isNotNull()
        .isNotEmpty()
        .contains("Goodbye");

    Moxy.getMoxyEngine().resetDefaultReturnTypes();
  }
}
