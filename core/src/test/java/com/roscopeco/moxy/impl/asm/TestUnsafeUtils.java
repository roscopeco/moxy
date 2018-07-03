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

package com.roscopeco.moxy.impl.asm;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class TestUnsafeUtils {
  @Test
  public void testUnsigned() {
    assertThat(UnsafeUtils.unsigned(0)).isEqualTo(0L);
    assertThat(UnsafeUtils.unsigned(1)).isEqualTo(1L);
    assertThat(UnsafeUtils.unsigned(-1)).isEqualTo(4294967295L);
    assertThat(UnsafeUtils.unsigned(Integer.MIN_VALUE)).isEqualTo(2147483648L);
  }

  @Test
  public void testFindDeclaredField() {
    final Field subField = UnsafeUtils.findDeclaredField(DestClassSubclass.class, "newBool", boolean.class);
    final Field inheritField = UnsafeUtils.findDeclaredField(DestClassSubclass.class, "byteField", byte.class);

    assertThat(subField).isNotNull();
    assertThat(subField.getName()).isEqualTo("newBool");
    assertThat(subField.getDeclaringClass()).isEqualTo(DestClassSubclass.class);

    assertThat(inheritField).isNotNull();
    assertThat(inheritField.getName()).isEqualTo("byteField");
    assertThat(inheritField.getDeclaringClass()).isEqualTo(DestClass1.class);
  }

  @Test
  public void testObjectCopy() throws InstantiationException {
    final SourceClass sc = new SourceClass((byte)0, 'a', (short)1, 2, 3L, 4.0f, 5.0d, true, "MARKER");
    final DestClass1 dc = UnsafeUtils.objectCopy(sc, UnsafeUtils.allocateInstance(DestClass1.class));

    assertThat(dc.byteField).isEqualTo((byte)0);
    assertThat(dc.charField).isEqualTo('a');
    assertThat(dc.shortField).isEqualTo((short)1);
    assertThat(dc.intField).isEqualTo(2);
    assertThat(dc.longField).isEqualTo((long)3);
    assertThat(dc.floatField).isEqualTo(4.0f);
    assertThat(dc.doubleField).isEqualTo(5.0d);
    assertThat(dc.boolField).isEqualTo(true);
    assertThat(dc.objectField).isEqualTo("MARKER");
  }

  public static class SourceClass {
    private final byte byteField;
    private final char charField;
    private final short shortField;
    private final int intField;
    private final long longField;
    private final float floatField;
    private final double doubleField;
    private final boolean boolField;
    private final Object objectField;

    public SourceClass(final byte b, final char c, final short s, final int i, final long l, final float f, final double d, final boolean bool, final Object o) {
      this.byteField = b;
      this.charField = c;
      this.shortField = s;
      this.intField = i;
      this.longField = l;
      this.floatField = f;
      this.doubleField = d;
      this.boolField = bool;
      this.objectField = o;
    }
  }

  public static class DestClass1 {
    private final byte byteField;
    private final char charField;
    private final short shortField;
    private final int intField;
    private final long longField;
    private final float floatField;
    private final double doubleField;
    private final boolean boolField;
    private final Object objectField;

    public DestClass1(final byte b, final char c, final short s, final int i, final long l, final float f, final double d, final boolean bool, final Object o) {
      this.byteField = b;
      this.charField = c;
      this.shortField = s;
      this.intField = i;
      this.longField = l;
      this.floatField = f;
      this.doubleField = d;
      this.boolField = bool;
      this.objectField = o;
    }
  }

  public static class DestClassSubclass extends DestClass1 {
    private final boolean newBool;

    public DestClassSubclass(final byte b, final char c, final short s, final int i, final long l, final float f, final double d, final boolean bool, final Object o, final boolean newBool) {
      super(b, c, s, i, l, f, d, bool, o);
      this.newBool = newBool;
    }
  }
}
