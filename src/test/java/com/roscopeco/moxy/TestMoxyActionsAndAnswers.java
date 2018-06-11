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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.matchers.Matchers;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestMoxyActionsAndAnswers {
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testMoxyMockThenAnswerWorksCorrectly() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    Moxy.when(() -> mock.sayHelloTo("Steve")).thenAnswer(args -> "Bonjour, " + args.get(0));

    assertThat(mock.sayHelloTo("Steve")).isEqualTo("Bonjour, Steve");
  }

  @Test
  public void testMoxyMockThenDoWorksCorrectly() {
    final MethodWithArguments mock = Moxy.mock(MethodWithArguments.class);

    final String[] string = new String[1];

    assertThat(string[0]).isNull();

    Moxy.when(() -> mock.hasArgs("one", "two")).thenDo(args -> string[0] = args.get(0).toString());

    mock.hasArgs("one", "two");

    assertThat(string[0]).isEqualTo("one");
  }

  @Test
  public void testMoxyMockThenDoThenAnswerWorksCorrectly() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final String[] string = new String[1];

    assertThat(string[0]).isNull();

    Moxy.when(() -> mock.sayHelloTo(Matchers.any()))
        .thenDo(args -> string[0] = args.get(0).toString())
        .thenAnswer(args -> "Hallo, " + args.get(0));

    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hallo, Bill");

    assertThat(string[0]).isEqualTo("Bill");
  }

  @Test
  public void testMoxyMockThenDoMultipleThenAnswerWorksCorrectly() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final String[] string = new String[3];

    assertThat(string[0]).isNull();

    Moxy.when(() -> mock.sayHelloTo(Matchers.any()))
        .thenDo(args -> string[0] = args.get(0).toString())
        .thenDo(args -> string[1] = args.get(0).toString().toLowerCase())
        .thenDo(args -> string[2] = args.get(0).toString().toUpperCase())
        .thenAnswer(args -> "Hallo, " + args.get(0));

    assertThat(mock.sayHelloTo("Bill")).isEqualTo("Hallo, Bill");

    assertThat(string[0]).isEqualTo("Bill");
    assertThat(string[1]).isEqualTo("bill");
    assertThat(string[2]).isEqualTo("BILL");
  }

  @Test
  public void testMoxyMockThenDoMultipleMatchesAllAreExecuted_whichMaySeemOdd_butIsDocumented() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    final String[] string = new String[2];

    assertThat(string[0]).isNull();

    Moxy.when(() -> mock.hasTwoArgs(Matchers.any(), Matchers.eqInt(5)))
      .thenDo(args -> string[0] = args.get(0).toString())
      .thenAnswer(args -> "Yo, " + args.get(0));

    Moxy.when(() -> mock.hasTwoArgs(Matchers.eq("Bill"), Matchers.anyInt()))
      .thenDo(args -> string[1] = args.get(1).toString())
      .thenAnswer(args -> "Yo, " + args.get(0));

    assertThat(mock.hasTwoArgs("Bill", 5)).isEqualTo("Yo, Bill");

    assertThat(string[0]).isEqualTo("Bill");
    assertThat(string[1]).isEqualTo("5");
  }
}
