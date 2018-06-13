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
package com.roscopeco.moxy.api;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestInvalidStubbingException {
  public static final String FORMAT = "format: [%s]";
  public static final String MARKER = "MARKER";
  public static final String FORMAT_RESULT = "format: [MARKER]";
  public static final Exception CAUSE = new Exception("CAUSE");

  @Test
  public void testStringConstructor() {
    final InvalidStubbingException ex = new InvalidStubbingException(MARKER);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringStringConstructor() {
    final InvalidStubbingException ex = new InvalidStubbingException(FORMAT, MARKER);

    assertThat(ex.getMessage()).isEqualTo(FORMAT_RESULT);
    assertThat(ex.getCause()).isNull();
  }

  @Test
  public void TestStringThrowableConstructor() {
    final InvalidStubbingException ex = new InvalidStubbingException(MARKER, CAUSE);

    assertThat(ex.getMessage()).isEqualTo(MARKER);
    assertThat(ex.getCause()).isEqualTo(CAUSE);
  }
}
