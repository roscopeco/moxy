package com.roscopeco.moxy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.model.SimpleConstructorClass;

public class TestMoxyConstructMocksAndSpies {
  @Test
  public void testMoxyConstructMockWorksWithNullConstructor() {
    final SimpleConstructorClass mock = Moxy.constructMock(SimpleConstructorClass.class);

    Moxy.when(() -> mock.returnString()).thenCallRealMethod();

    assertThat(mock.returnString()).isEqualTo("Hello");
  }

  @Test
  public void testMoxyConstructMockWorksWithOneArgConstructor() {
    final SimpleConstructorClass mock =
        Moxy.constructMock(SimpleConstructorClass.class, "Goodbye");

    Moxy.when(() -> mock.returnString()).thenCallRealMethod();

    assertThat(mock.returnString()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyConstructMockWorksWithTwoArgConstructor() {
    final SimpleConstructorClass mock =
        Moxy.constructMock(SimpleConstructorClass.class, "Good", "bye");

    Moxy.when(() -> mock.returnString()).thenCallRealMethod();

    assertThat(mock.returnString()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyConstructMockWorksWithPrimitiveArgConstructor() {
    final SimpleConstructorClass mock =
        Moxy.constructMock(SimpleConstructorClass.class, "Level", 42);

    Moxy.when(() -> mock.returnString()).thenCallRealMethod();

    assertThat(mock.returnString()).isEqualTo("Level42");
  }

  @Test
  public void testMoxyConstructSpyWorksWithNullConstructor() {
    final SimpleConstructorClass mock = Moxy.constructSpy(SimpleConstructorClass.class);

    assertThat(mock.returnString()).isEqualTo("Hello");
  }

  @Test
  public void testMoxyConstructSpyWorksWithOneArgConstructor() {
    final SimpleConstructorClass mock =
        Moxy.constructSpy(SimpleConstructorClass.class, "Goodbye");

    assertThat(mock.returnString()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyConstructSpyWorksWithTwoArgConstructor() {
    final SimpleConstructorClass mock =
        Moxy.constructSpy(SimpleConstructorClass.class, "Good", "bye");

    assertThat(mock.returnString()).isEqualTo("Goodbye");
  }

  @Test
  public void testMoxyConstructSpyWorksWithPrimitiveArgConstructor() {
    final SimpleConstructorClass mock =
        Moxy.constructSpy(SimpleConstructorClass.class, "Level", 42);

    assertThat(mock.returnString()).isEqualTo("Level42");
  }
}
