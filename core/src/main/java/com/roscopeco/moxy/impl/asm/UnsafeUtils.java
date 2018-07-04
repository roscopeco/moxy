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
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

/**
 * 'Safer' wrapper around sun.misc.Unsafe.
 *
 * For some value of 'Safer', at least...
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class UnsafeUtils {
  private static final Logger LOG = Logger.getLogger(UnsafeUtils.class.getName());

  private UnsafeUtils() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.as.UnsafeUtils is not designed for instantiation");
  }

  @SuppressWarnings("restriction")
  private static final sun.misc.Unsafe UNSAFE = initUnsafe();

  @SuppressWarnings("restriction")
  private static sun.misc.Unsafe initUnsafe() {
    try {
      final Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      unsafeField.setAccessible(true);
      return (sun.misc.Unsafe) unsafeField.get(null);
    } catch (final NoSuchFieldException e) {
      throw new IllegalStateException("Unrecoverable Error: NoSuchFieldException 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    } catch (final IllegalAccessException e) {
      throw new IllegalStateException(
          "Unrecoverable Error: IllegalAccessException when accessing 'theUnsafe' on sun.misc.Unsafe.\n"
              + "This is most likely an environment issue.",
          e);
    }
  }

  /**
   * Define a class given a loader, name and a byte(code) array.
   *
   * @param loader
   *          ClassLoader to define in.
   * @param name
   *          Class name (must match bytecode).
   * @param code
   *          The bytecode.
   *
   * @return The newly-defined class.
   */
  public static Class<?> defineClass(final ClassLoader loader, final String name, final byte[] code) {
    return defineClass(loader, name, code, true);
  }

  /**
   * Define a class given a loader, name and a byte(code) array.
   *
   * If true is passed for the systemLoaderCheck parameter, an exception will be
   * thrown if loader is null.
   *
   * @param loader
   *          ClassLoader to define in.
   * @param name
   *          Class name (must match bytecode).
   * @param code
   *          The bytecode.
   * @param bootLoaderCheck
   *          if <code>true</code>, an exception will be thrown if loader is null.
   *
   * @return The newly-defined class.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static Class<?> defineClass(final ClassLoader loader, final String name, final byte[] code,
      final boolean bootLoaderCheck) {
    if (bootLoaderCheck && loader == null) {
      LOG.warning(() -> "Implicit definition in the system classloader;\n"
          + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
          + "If you are using class mocking for system classes, be prepared for undefined behaviour.");
    }

    return UNSAFE.defineClass(name.replace('/', '.'), code, 0, code.length, loader, null);
  }

  /**
   * Wraps sun.misc.Unsafe.putObjectField.
   *
   * @param receiver
   *          The object with the field to set.
   * @param fieldOffset
   *          The field offset.
   * @param value
   *          The value
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
   * @param field
   *          Field to get offset for.
   *
   * @return The field offset.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static long objectFieldOffset(final Field field) {
    return UNSAFE.objectFieldOffset(field);
  }

  /**
   * Allocate an instance of the given class without calling a constructor.
   *
   * @param clz The class to instantiate.
   *
   * @return A new instance.
   *
   * @throws InstantiationException if an error occurs during allocation.
   * @param <T> The type of instance being allocated.
   */
  @SuppressWarnings({ "restriction", "deprecation", "unchecked" })
  public static <T> T allocateInstance(final Class<T> clz) throws InstantiationException {
    return (T)UNSAFE.allocateInstance(clz);
  }

  static Field findDeclaredField(final Class<?> clz, final String name, final Class<?> type) {
    try {
      final Field f = clz.getDeclaredField(name);

      if (f.getType().isAssignableFrom(type)) {
        return f;
      }
    } catch (final Exception ex) {
      /* do nothing */
    }

    if (clz.getSuperclass() == null) {
      return null;
    } else {
      return findDeclaredField(clz.getSuperclass(), name, type);
    }
  }

  static long unsigned(final int value) {
    if (value >= 0) {
      return value;
    }
    return (~0L >>> 32) & value;
  }

  /**
   * Returns the address of the given object.
   *
   * @param obj The object
   * @return the address of the object
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static long toAddress(final Object obj) {
    final Object[] array = new Object[] {obj};
    final long baseOffset = UNSAFE.arrayBaseOffset(Object[].class);
    return unsigned(UNSAFE.getInt(array, baseOffset));
  }

  /**
   * <p>Copy the given object's fields to the destination object.</p>
   *
   * <p>If the two objects are of the same class, this will copy all
   * fields. If not, copying will be on a 'best-effort' basis,
   * with only fields of matching name and type being copied.</p>
   *
   * @param src The source object
   * @param dest The destination object
   *
   * @return The destination, for convenience.
   * @param <T> The destination type.
   */
  @SuppressWarnings({ "restriction", "deprecation" })
  public static <T> T objectCopy(final Object src, final T dest) {
    final sun.misc.Unsafe unsafe = UnsafeUtils.UNSAFE;

    Class<?> srcClz = src.getClass();
    final Class<?> destClz = dest.getClass();

    while (srcClz != Object.class) {
      for (final Field srcField : srcClz.getDeclaredFields()) {
        if ((srcField.getModifiers() & Modifier.STATIC) == 0) {
          final Class<?> type = srcField.getType();
          Field destField;

          if (destClz.equals(srcClz)) {
            // copying same type
            destField = srcField;
          } else {
            // types are different
            destField = findDeclaredField(destClz, srcField.getName(), srcField.getType());
          }

          if (destField != null) {
            final long srcOffset = unsafe.objectFieldOffset(srcField);
            final long destOffset = unsafe.objectFieldOffset(destField);

            if (type == byte.class) {
              unsafe.putByte(dest, destOffset, unsafe.getByte(src, srcOffset));
            } else if (type == char.class) {
              unsafe.putChar(dest, destOffset, unsafe.getChar(src, srcOffset));
            } else if (type == short.class) {
              unsafe.putShort(dest, destOffset, unsafe.getShort(src, srcOffset));
            } else if (type == int.class) {
              unsafe.putInt(dest, destOffset, unsafe.getInt(src, srcOffset));
            } else if (type == long.class) {
              unsafe.putLong(dest, destOffset, unsafe.getLong(src, srcOffset));
            } else if (type == float.class) {
              unsafe.putFloat(dest, destOffset, unsafe.getFloat(src, srcOffset));
            } else if (type == double.class) {
              unsafe.putDouble(dest, destOffset, unsafe.getDouble(src, srcOffset));
            } else if (type == boolean.class) {
              unsafe.putBoolean(dest, destOffset, unsafe.getBoolean(src, srcOffset));
            } else {
              unsafe.putObject(dest, destOffset, unsafe.getObject(src, srcOffset));
            }
          }
        }
      }
      srcClz = srcClz.getSuperclass();
    }

    return dest;
  }
}
