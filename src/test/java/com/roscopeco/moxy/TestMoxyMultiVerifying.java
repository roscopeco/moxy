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
import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

import com.roscopeco.moxy.api.InvalidMockInvocationException;
import com.roscopeco.moxy.api.MoxyMultiVerifier;
import com.roscopeco.moxy.matchers.Matchers;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.MethodWithArgAndReturn;
import com.roscopeco.moxy.model.MethodWithPrimitiveArguments;

public class TestMoxyMultiVerifying {
  private static final String EOL = System.lineSeparator();

  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testMoxyMockWithMockAssertMocksFailsWithNoInvocation() {
    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> { /* nothing happening here... */ })
    )
        .isInstanceOf(InvalidMockInvocationException.class)
        .hasMessage("No mock invocation found");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksReturnsMultiVerifier() {
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    final MoxyMultiVerifier multi = Moxy.assertMocks(() -> {
      mock.returnByte();
      mock.returnFloat();
    });

    assertThat(multi).isNotNull();
  }

  @Test
  public void testMoxyMockWithMockAssertMocksNoneCalledWorks() {
    Moxy.getMoxyEngine().reset();
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    // Note - this is testing that we don't just take arguments into account
    // when matching, but that we also take the method name/descriptor into account
    // too.
    mock.returnBoolean();
    mock.returnDouble();

    Moxy.assertMocks(() -> {
      mock.returnByte();
      mock.returnFloat();
    })
        .wereNotCalled();

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.returnByte();
          mock.returnBoolean();
          mock.returnFloat();
        })
            .wereNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnBoolean() to be called exactly zero times, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.returnDouble();
          mock.returnBoolean();
        })
            .wereNotCalled()
    )
        .isInstanceOf(MultipleFailuresError.class)
        .hasMessage("There were unexpected invocations (2 failures)" + EOL
            + "\tExpected mock returnDouble() to be called exactly zero times, but it was called once" + EOL
            + "\tExpected mock returnBoolean() to be called exactly zero times, but it was called once");

  }

  @Test
  public void testMoxyMockWithMockAssertMocksNoneCalledWithArgumentsWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 1);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Steve");
      mock.hasTwoArgs("two", 2);
    })
        .wereNotCalled();

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Bill");
          mock.hasTwoArgs("one", 1);
        })
            .wereNotCalled()
    )
        .isInstanceOf(MultipleFailuresError.class)
        .hasMessage("There were unexpected invocations (2 failures)" + EOL
                  + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly zero times, but it was called once" + EOL
                  + "\tExpected mock hasTwoArgs(\"one\", 1) to be called exactly zero times, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Bill");
          mock.hasTwoArgs("two", 2);
        })
            .wereNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock sayHelloTo(\"Bill\") to be called exactly zero times, but it was called once");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Steve");
          mock.hasTwoArgs("one", 1);
        })
            .wereNotCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock hasTwoArgs(\"one\", 1) to be called exactly zero times, but it was called once");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksNoneCalledWithAllPrimitiveTypes() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    mock.hasArgs("test", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false);

    Moxy.assertMocks(() -> {
      mock.hasArgs("TEST", (byte)2, 'b', (short)4, 5, 6L, 7.0f, 8.0d, true);
    }).wereNotCalled();

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.hasArgs("test", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false);
      }).wereNotCalled()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock hasArgs(\"test\", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false) to be called exactly zero times, but it was called once");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksAllCalledWorks() {
    Moxy.getMoxyEngine().reset();
    final ClassWithPrimitiveReturns mock = Moxy.mock(ClassWithPrimitiveReturns.class);

    // Note - this is testing that we don't just take arguments into account
    // when matching, but that we also take the method name/descriptor into account
    // too.
    mock.returnBoolean();
    mock.returnDouble();

    Moxy.assertMocks(() -> {
      mock.returnBoolean();
      mock.returnDouble();
    })
        .wereAllCalled();

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.returnByte();
          mock.returnBoolean();
        })
            .wereAllCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock returnByte() to be called at least once, but it was called zero times");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.returnByte();
          mock.returnFloat();
        })
            .wereAllCalled()
    )
        .isInstanceOf(MultipleFailuresError.class)
        .hasMessage("There were unexpected invocations (2 failures)" + EOL
            + "\tExpected mock returnByte() to be called at least once, but it was called zero times" + EOL
            + "\tExpected mock returnFloat() to be called at least once, but it was called zero times");

  }

  @Test
  public void testMoxyMockWithMockAssertMocksAllCalledWithArgumentsWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 1);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 1);
    })
        .wereAllCalled();

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Steve");
          mock.hasTwoArgs("two", 2);
        })
            .wereAllCalled()
    )
        .isInstanceOf(MultipleFailuresError.class)
        .hasMessage("There were unexpected invocations (2 failures)" + EOL
                  + "\tExpected mock sayHelloTo(\"Steve\") to be called at least once, but it was called zero times" + EOL
                  + "\tExpected mock hasTwoArgs(\"two\", 2) to be called at least once, but it was called zero times");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Steve");
          mock.hasTwoArgs("one", 1);
        })
            .wereAllCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock sayHelloTo(\"Steve\") to be called at least once, but it was called zero times");

    assertThatThrownBy(() ->
        Moxy.assertMocks(() -> {
          mock.sayHelloTo("Bill");
          mock.hasTwoArgs("two", 2);
        })
            .wereAllCalled()
    )
        .isInstanceOf(AssertionFailedError.class)
        .hasMessage("Expected mock hasTwoArgs(\"two\", 2) to be called at least once, but it was called zero times");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksAllCalledWithAllPrimitiveTypes() {
    final MethodWithPrimitiveArguments mock = Moxy.mock(MethodWithPrimitiveArguments.class);

    mock.hasArgs("test", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false);

    Moxy.assertMocks(() -> {
      mock.hasArgs("test", (byte)1, 'a', (short)3, 4, 5L, 6.0f, 7.0d, false);
    }).wereAllCalled();

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.hasArgs("TEST", (byte)2, 'b', (short)4, 5, 6L, 7.0f, 8.0d, true);
      }).wereAllCalled()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock hasArgs(\"TEST\", (byte)2, 'b', (short)4, 5, 6L, 7.0f, 8.0d, true) to be called at least once, but it was called zero times");
  }

  @Test
  public void testMoxyMockWithMockAssertMocksAllCalledWithNullArguments() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo(null);
      }).wereAllCalled()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(null) to be called at least once, but it was called zero times");

    mock.sayHelloTo(null);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo(null);
    }).wereAllCalled();
  }

  @Test
  public void testMoxyAssertMockWithMockAllCalledExactNumberOfTimesWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
    }).wereAllCalledExactly(1);

    Moxy.assertMocks(() -> {
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledExactly(1);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledExactly(1);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Ben");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledExactly(1)
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(\"Ben\") to be called exactly once, but it was called zero times");


    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("three", 4);
      }).wereAllCalledExactly(1)
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock hasTwoArgs(\"three\", 4) to be called exactly once, but it was called zero times");

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Ben");
        mock.hasTwoArgs("three", 4);
      }).wereAllCalledExactly(1)
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Ben\") to be called exactly once, but it was called zero times" + EOL
          + "\tExpected mock hasTwoArgs(\"three\", 4) to be called exactly once, but it was called zero times");

    mock.sayHelloTo("Bill");

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledExactly(1)
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock sayHelloTo(\"Bill\") to be called exactly once, but it was called twice");

    mock.hasTwoArgs("one", 2);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledExactly(1)
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly once, but it was called twice" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called exactly once, but it was called twice");
  }

  @Test
  public void testMoxyAssertMockWithMockAllCalledOnceWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledOnce()
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly once, but it was called zero times" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called exactly once, but it was called zero times");

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledOnce();

    mock.sayHelloTo("Ben");
    mock.hasTwoArgs("two", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledOnce();

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledOnce()
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly once, but it was called twice" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called exactly once, but it was called twice");
  }

  @Test
  public void testMoxyAssertMockWithMockAllCalledTwiceWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledTwice()
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly twice, but it was called zero times" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called exactly twice, but it was called zero times");

    mock.sayHelloTo("Bill");
    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);
    mock.hasTwoArgs("one", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledTwice();

    mock.sayHelloTo("Ben");
    mock.hasTwoArgs("two", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledTwice();

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledTwice()
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called exactly twice, but it was called 3 times" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called exactly twice, but it was called 3 times");
  }

  @Test
  public void testMoxyAssertMockWithMockAllCalledAtLeastNumAndTwiceWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    mock.sayHelloTo("Ben");
    mock.hasTwoArgs("two", 2);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledAtLeast(2)
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called at least twice, but it was called once" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called at least twice, but it was called once");

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledAtLeastTwice()
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called at least twice, but it was called once" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called at least twice, but it was called once");

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtLeast(2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtLeastTwice();

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtLeast(2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtLeastTwice();
  }

  @Test
  public void testMoxyAssertMockWithMockAllCalledAtMostNumAndTwiceWorks() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    // Notable case - passes even if called zero times.
    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtMost(2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtMostTwice();

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtMost(2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtMostTwice();

    mock.sayHelloTo("Ben");
    mock.hasTwoArgs("two", 2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtMost(2);

    Moxy.assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 2);
    }).wereAllCalledAtMostTwice();

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 2);

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledAtMost(2)
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called at most twice, but it was called 3 times" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called at most twice, but it was called 3 times");

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Bill");
        mock.hasTwoArgs("one", 2);
      }).wereAllCalledAtMostTwice()
    )
      .isInstanceOf(MultipleFailuresError.class)
      .hasMessage("There were unexpected invocations (2 failures)" + EOL
          + "\tExpected mock sayHelloTo(\"Bill\") to be called at most twice, but it was called 3 times" + EOL
          + "\tExpected mock hasTwoArgs(\"one\", 2) to be called at most twice, but it was called 3 times");
  }

  @Test
  public void testMoxyAssertMockWithMockAllCalledFamilyWorksWithMatchers() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.sayHelloTo("Joe");

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo(Matchers.any());
        mock.hasTwoArgs(Matchers.regexMatch("..zz"), Matchers.ltInt(3));
      }).wereAllCalled()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock hasTwoArgs(<regex: ..zz>, <lt: 3>) to be called at least once, but it was called zero times");

    mock.hasTwoArgs("fizz", 5); // doesn't match

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo(Matchers.any());
        mock.hasTwoArgs(Matchers.regexMatch("..zz"), Matchers.ltInt(3));
      }).wereAllCalled()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected mock hasTwoArgs(<regex: ..zz>, <lt: 3>) to be called at least once, but it was called zero times");

    mock.hasTwoArgs("fizz", 2); // matches

    Moxy.assertMocks(() -> {
      mock.sayHelloTo(Matchers.any());
      mock.hasTwoArgs(Matchers.regexMatch("..zz"), Matchers.ltInt(3));
    }).wereAllCalled();
  }

  @Test
  public void testMoxyMockWithMockAssertMocksInThatOrder() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.hasTwoArgs("Bill", 2);
    mock.hasTwoArgs("Norman", 2);     // This is fine - non-exclusive order.
    mock.sayHelloTo("Steve");

    Moxy.assertMocks(() -> {
      mock.hasTwoArgs("Bill", 2);
      mock.sayHelloTo("Steve");
    }).wereAllCalled()
      .inThatOrder();

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Steve");
        mock.hasTwoArgs("Bill", 2);
      }).wereAllCalled()
        .inThatOrder()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected invocations:\n"
                + "\tsayHelloTo(\"Steve\")\n"
                + "\thasTwoArgs(\"Bill\", 2)\n"
                + "in that order, but they were invoked out of order");

    // Check we get proper fail message if not checked all called
    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.sayHelloTo("Steve");
        mock.hasTwoArgs("Bill", 2);
      }).inThatOrder()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected invocations:\n"
                + "\tsayHelloTo(\"Steve\")\n"
                + "\thasTwoArgs(\"Bill\", 2)\n"
                + "in that order, but they were not invoked or were invoked out of order");

  }

  @Test
  public void testMoxyMockWithMockAssertMocksExclusivelyInThatOrder() {
    final MethodWithArgAndReturn mock = Moxy.mock(MethodWithArgAndReturn.class);

    mock.hasTwoArgs("Bill", 2);
    mock.sayHelloTo("Steve");

    Moxy.assertMocks(() -> {
      mock.hasTwoArgs("Bill", 2);
      mock.sayHelloTo("Steve");
    }).wereAllCalled()
      .exclusivelyInThatOrder();

    mock.hasTwoArgs("Joe", 5);
    mock.hasTwoArgs("Bill", 42);
    mock.sayHelloTo("Keith");

    assertThatThrownBy(() ->
      Moxy.assertMocks(() -> {
        mock.hasTwoArgs("Joe", 5);
        mock.sayHelloTo("Keith");
      }).wereAllCalled()
        .exclusivelyInThatOrder()
    )
      .isInstanceOf(AssertionFailedError.class)
      .hasMessage("Expected invocations:\n"
                + "\thasTwoArgs(\"Joe\", 5)\n"
                + "\tsayHelloTo(\"Keith\")\n"
                + "exclusively in that order, but they were invoked out of order");
  }
}
