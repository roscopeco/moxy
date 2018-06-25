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

import java.lang.reflect.Field;

/**
 * TODO Document UnsafeUtils
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class UnsafeUtils {
  @SuppressWarnings("restriction")
  private static final sun.misc.Unsafe UNSAFE = initUnsafe();

  @SuppressWarnings("restriction")
  private static sun.misc.Unsafe initUnsafe() {
    try {
      final Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      unsafeField.setAccessible(true);
      return (sun.misc.Unsafe)unsafeField.get(null);
    } catch (final NoSuchFieldException e) {
      throw new IllegalStateException("Unrecoverable Error: NoSuchFieldException 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    } catch (final IllegalAccessException e) {
      throw new IllegalStateException("Unrecoverable Error: IllegalAccessException when accessing 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    }
  }

  /**
   * Define a class given a loader, name and a byte(code) array.
   *
   * @param loader ClassLoader to define in.
   * @param name Class name (must match bytecode).
   * @param code The bytecode.
   *
   * @return The newly-defined class.
   */
  public static Class<?> defineClass(final ClassLoader loader, final String name, final byte[] code) {
    return defineClass(loader, name, code, true);
  }

  /**
   * Define a class given a loader, name and a byte(code) array.
   *
   * If true is passed for the systemLoaderCheck parameter, an exception will
   * be thrown if loader is null.
   *
   * @param loader ClassLoader to define in.
   * @param name Class name (must match bytecode).
   * @param code The bytecode.
   * @param bootLoaderCheck if <code>true</code>, an exception will be thrown if loader is null.
   *
   * @return The newly-defined class.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static Class<?> defineClass(final ClassLoader loader, final String name, final byte[] code, final boolean bootLoaderCheck) {
    if (bootLoaderCheck && loader == null) {
      throw new IllegalArgumentException("Implicit definition in the system classloader is unsupported.\n"
                                       + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
                                       + "If you're sure this is what you want to do, pass system loader explicitly (rather than null)");
    }

    return UNSAFE.defineClass(name.replace('/',  '.'), code, 0, code.length, loader, null);
  }

  /**
   * Wraps sun.misc.Unsafe.putObjectField.
   *
   * @param receiver The object with the field to set.
   * @param fieldOffset The field offset.
   * @param value The value
   *
   * @see #objectFieldOffset(Field)
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static void putObject(final Object receiver, final long fieldOffset, final Object value) {
    UNSAFE.putObject(receiver, fieldOffset, value);
  }

  /**
   * Wraps sun.misc.Unsafe.objectFieldOffset.
   *
   * @param field Field to get offset for.
   *
   * @return The field offset.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static long objectFieldOffset(final Field field) {
    return UNSAFE.objectFieldOffset(field);
  }

  /**
   * Allocate an instance of the given class without calling
   * a constructor.
   *
   * @param clz The class to instantiate.
   *
   * @return A new instance.
   *
   * @throws InstantiationException if an error occurs during allocation.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static Object allocateInstance(final Class<?> clz) throws InstantiationException {
    return UNSAFE.allocateInstance(clz);
  }
}