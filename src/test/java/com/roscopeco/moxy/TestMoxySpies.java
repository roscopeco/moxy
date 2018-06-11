package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.matchers.Matchers;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;
import com.roscopeco.moxy.model.SimpleClass;

public class TestMoxySpies {
  @Test
  public void testMoxySpyCreatesASpyForNoArgMethods() {
    final SimpleClass spy = Moxy.spy(SimpleClass.class);

    assertThat(Moxy.isMock(spy)).isTrue();
    assertThat(spy.returnHello()).isEqualTo("Hello");

    Moxy.assertMock(() -> spy.returnHello()).wasCalledOnce();
  }

  @Test
  public void testMoxySpyClassCreatesASpyWithObjectArguments() {
    final MethodWithArguments spy = Moxy.spy(MethodWithArguments.class);

    assertThat(Moxy.isMock(spy)).isTrue();

    spy.hasArgs("one", "two");

    Moxy.assertMock(() -> spy.hasArgs("one", "two")).wasCalledOnce();
    Moxy.assertMock(() -> spy.hasArgs("three", "four")).wasNotCalled();
    Moxy.assertMock(() -> spy.hasArgs(Matchers.any(), Matchers.any())).wasCalledOnce();
  }

  @Test
  public void testMoxySpyClassCreatesASpyWithMixedObjectAndIntArgs() {
    final MethodWithArgAndReturn spy = Moxy.spy(MethodWithArgAndReturn.class);

    assertThat(Moxy.isMock(spy)).isTrue();
    assertThat(spy.hasTwoArgs("Level", 42)).isEqualTo("Level42");
    assertThat(spy.sayHelloTo("Bill")).isEqualTo("Hello, Bill");

    Moxy.assertMock(() -> spy.hasTwoArgs("Level", 42)).wasCalledOnce();
    Moxy.assertMock(() -> spy.hasTwoArgs("Nonsense", 99)).wasNotCalled();
    Moxy.assertMock(() -> spy.sayHelloTo("Bill")).wasCalledOnce();
    Moxy.assertMock(() -> spy.sayHelloTo("Steve")).wasNotCalled();
  }

  @Test
  public void testMoxySpyClassCreatesASpyWithAllPrimitiveTypeArguments() {
    final MethodWithPrimitiveArguments spy = Moxy.spy(MethodWithPrimitiveArguments.class);

    assertThat(Moxy.isMock(spy)).isTrue();
    assertThat(spy.hasArgs("one",
                           (byte)10,
                           'a',
                           (short)11,
                           12,
                           13L,
                           14.0f,
                           15.0d,
                           true)).isEqualTo(42);

    Moxy.assertMock(() -> spy.hasArgs("one",
                                      (byte)10,
                                      'a',
                                      (short)11,
                                      12,
                                      13L,
                                      14.0f,
                                      15.0d,
                                      true)
    ).wasCalledOnce();

    Moxy.assertMock(() -> spy.hasArgs("two",
                                      (byte)10,
                                      'a',
                                      (short)11,
                                      12,
                                      13L,
                                      14.0f,
                                      15.0d,
                                      true)
    ).wasNotCalled();

    Moxy.assertMock(() -> spy.hasArgs(Matchers.any(),
                                      Matchers.anyByte(),
                                      Matchers.anyChar(),
                                      Matchers.anyShort(),
                                      Matchers.anyInt(),
                                      Matchers.anyLong(),
                                      Matchers.anyFloat(),
                                      Matchers.anyDouble(),
                                      Matchers.anyBool())
    ).wasCalledOnce();

  }

  @Test
  public void testMoxySpyObjectFailsToConvertNonMock() {
    assertThatThrownBy(() -> Moxy.spy(new Object()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Cannot convert java\\.lang\\.Object@[0-9a-f]+ to spy - it is not a mock");
  }

  @Test
  void testMoxySpyObjectCanConvertExistingMock() {
    // Given I have a mock
    final SimpleClass mock = Moxy.mock(SimpleClass.class);
    assertThat(mock.returnHello()).isNull();

    // When I convert it to a spy
    final SimpleClass spy = Moxy.spy(mock);

    // Then the spy calls its real methods
    assertThat(spy.returnHello()).isEqualTo("Hello");

    // And the spy is not a copy
    assertThat(spy).isSameAs(mock);
    assertThat(mock.returnHello()).isEqualTo("Hello");
  }

  @Test
  public void testMoxySpyObjectCanConvertExistingMockAfterStubbing() {
    // Given I have a mock
    final SimpleClass mock = Moxy.mock(SimpleClass.class);
    assertThat(mock.returnHello()).isNull();

    // And it has existing stubbing
    Moxy.when(() -> mock.returnHello()).thenReturn("Goodbye");
    assertThat(mock.returnHello()).isEqualTo("Goodbye");

    // When I convert it to a spy
    final SimpleClass spy = Moxy.spy(mock);

    // Then the stubbing is discarded and it becomes a spy
    assertThat(spy.returnHello()).isEqualTo("Hello");
  }
}
