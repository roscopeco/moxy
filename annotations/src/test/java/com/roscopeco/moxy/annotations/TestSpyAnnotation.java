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

package com.roscopeco.moxy.annotations;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.annotations.model.TesterClass;

public class TestSpyAnnotation {
  @BeforeEach
  public void beforeEach() {
    MoxyAnnotations.initMocks(this);
  }

  @Spy
  public TesterClass publicSpy;

  @Spy
  protected TesterClass protectedSpy;

  @Spy
  protected TesterClass packageSpy;

  @Spy
  private TesterClass privateSpy;

  @Test
  public void testSetsPublicSpy() {
    assertThat(this.publicSpy).isNotNull();
    assertThat(Moxy.isMock(this.publicSpy)).isTrue();

    // Make sure it's actually a spy...
    assertThat(this.publicSpy.test()).isEqualTo(TesterClass.PASSED);
  }

  @Test
  public void testSetsProtectedMock() {
    assertThat(this.protectedSpy).isNotNull();
    assertThat(Moxy.isMock(this.protectedSpy)).isTrue();

    // Make sure it's actually a spy...
    assertThat(this.publicSpy.test()).isEqualTo(TesterClass.PASSED);
  }

  @Test
  public void testSetsPackageMock() {
    assertThat(this.packageSpy).isNotNull();
    assertThat(Moxy.isMock(this.packageSpy)).isTrue();

    // Make sure it's actually a spy...
    assertThat(this.publicSpy.test()).isEqualTo(TesterClass.PASSED);
  }

  @Test
  public void testSetsPrivateMock() {
    assertThat(this.privateSpy).isNotNull();
    assertThat(Moxy.isMock(this.privateSpy)).isTrue();

    // Make sure it's actually a spy...
    assertThat(this.publicSpy.test()).isEqualTo(TesterClass.PASSED);
  }
}
