package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.matchers.Matchers;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;

public class TestMoxyActionsAndAnswers {
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
