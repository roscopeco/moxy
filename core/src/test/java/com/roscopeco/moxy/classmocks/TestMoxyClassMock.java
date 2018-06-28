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

package com.roscopeco.moxy.classmocks;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.model.FinalClass;
import com.roscopeco.moxy.model.SimpleClass;

public class TestMoxyClassMock {
  public static class Delegate {
    public String returnHello() {
      return "Delegated";
    }
  }

  @Test
  public void testMoxyClassMockCanMockSimpleClass() throws Exception {
    Moxy.mockClasses(SimpleClass.class);

    final SimpleClass sc = new SimpleClass();

    assertThat(sc.returnHello()).isNull();

    assertThat(Moxy.isMock(FinalClass.class));
    assertThat(Moxy.isMock(sc));
  }

  @Test
  public void testMoxyClassMockCanResetMockedClass() throws Exception {
    Moxy.mockClasses(SimpleClass.class);

    final SimpleClass sc = new SimpleClass();

    assertThat(sc.returnHello()).isNull();

    assertThat(Moxy.isMock(FinalClass.class));
    assertThat(Moxy.isMock(sc));

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

    assertThat(Moxy.isMock(FinalClass.class));
    assertThat(Moxy.isMock(sc));
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
}
