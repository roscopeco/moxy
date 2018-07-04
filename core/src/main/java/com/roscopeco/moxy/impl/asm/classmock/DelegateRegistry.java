/*
 * InstanceRegistry.java -
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

package com.roscopeco.moxy.impl.asm.classmock;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.impl.asm.ASMMockInstanceVars;
import com.roscopeco.moxy.impl.asm.ASMMockSupport;
import com.roscopeco.moxy.impl.asm.ASMMoxyEngine;
import com.roscopeco.moxy.impl.asm.TypesAndDescriptors;
import com.roscopeco.moxy.impl.asm.UnsafeUtils;
import com.roscopeco.moxy.matchers.Matchers;

/**
 * <p>Manages mappings between class-mocked objects and their backing delegates.</p>
 *
 * <p>For instances, {@link #getDelegate(Object)} will return the instance
 * of the (copied) delegate class that backs the given instance. This is
 * a generated instance of a class copied directly from the original class,
 * and contains all the state and original methods.</p>
 *
 * <p>For statics, {@link #getStaticDelegate(Class)} will return a
 * <code>StaticDelegate</code> instance that just provides the mock support required
 * for e.g. recording invocations and applying stubbing. This instance
 * does not have any of the real methods or static state of the original
 * class.</p>
 *
 * <p>If the static delegate does not already exist, it will be created
 * automatically.</p>
 *
 * <p>This method of delegating statics works because mocked statics are
 * generated to statically call their real method if they are set to
 * do so.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class DelegateRegistry {
  private static final Logger LOG = Logger.getLogger(DelegateRegistry.class.getName());

  private static final WeakHashMap<Class<?>, String> delegateClassRegistryMap = new WeakHashMap<>();
  private static final WeakHashMap<Object, ASMMockSupport> instanceRegistryMap = new WeakHashMap<>();
  private static final WeakHashMap<Class<?>, ASMMockSupport> staticRegistryMap = new WeakHashMap<>();

  private DelegateRegistry() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.classmock.DelegateRegistry is not designed for instantiation");
  }

  public static void registerDelegateClass(final Class<?> forClz, final String delegateClzName) {
    synchronized (delegateClassRegistryMap) {
      delegateClassRegistryMap.put(forClz, delegateClzName);
    }
  }

  public static Class<?> getDelegateClass(final Class<?> forClz) {
    synchronized (delegateClassRegistryMap) {
      final String clzName = delegateClassRegistryMap.get(forClz);
      if (clzName == null) {
        return null;
      } else {
        try {
          return Class.forName(clzName);
        } catch (final ClassNotFoundException e) {
          throw new MoxyException("[BUG] Missing delegate class: " + clzName, e);
        }
      }
    }
  }

  public static void removeDelegateClass(final Class<?> forClz) {
    synchronized (delegateClassRegistryMap) {
      delegateClassRegistryMap.remove(forClz);
    }
  }

  public static void registerDelegate(final Object forObj, final ASMMockSupport delegate) {
    synchronized (instanceRegistryMap) {
      final Object old = instanceRegistryMap.put(forObj, delegate);

      if (old != null && old != delegate) {
        LOG.warning(() -> "Delegate changed for object '" + forObj + "' (from '" + old + "' to '" + delegate + "'");
      }
    }
  }

  public static ASMMockSupport getDelegate(final Object forObj) {
    synchronized (instanceRegistryMap) {
      final ASMMockSupport mock = instanceRegistryMap.get(forObj);

      // This instance must have been around before this class was mocked,
      // so create a new spy delegate, and copy all state to it.
      if (mock == null) {
        final String clzName = delegateClassRegistryMap.get(forObj.getClass());

        if (clzName == null) {
          throw new MoxyException("[BUG] No registered delegate class for object of class " + forObj.getClass());
        } else {
          try {
            return registerNewSpyingDelegate(Class.forName(clzName), forObj);
          } catch (final ClassNotFoundException e) {
            throw new MoxyException("[BUG] Missing delegate class: " + clzName, e);
          }
        }
      }

      return mock;
    }
  }

  static void clearStaticDelegate(final Class<?> forClz) {
    synchronized (staticRegistryMap) {
      staticRegistryMap.remove(forClz);
    }
  }

  public static ASMMockSupport getStaticDelegate(final Class<?> forClz) {
    synchronized (staticRegistryMap) {
      if (!staticRegistryMap.containsKey(forClz)) {
        staticRegistryMap.put(forClz, new StaticDelegate((ASMMoxyEngine)Moxy.getMoxyEngine()));
      }

      return staticRegistryMap.get(forClz);
    }
  }

  /*
   * Create a new delegate for the given object, and convert the original
   * to a spy that will call the delegate.
   */
  // TODO this shouldn't be here, it should be somewhere else.
  static ASMMockSupport registerNewSpyingDelegate(final Class<?> clz, final Object original) {
    ASMMockSupport delegate;

    try {
      delegate = (ASMMockSupport)UnsafeUtils.allocateInstance(clz);
    } catch (final InstantiationException e) {
      throw new MoxyException("Unable to instantiate spy for pre-existing instance of mocked class");
    }

    try {
      setMockInstanceVariables((ASMMoxyEngine)Moxy.getMoxyEngine(), delegate);
    } catch (final ClassCastException e) {
      // TODO Fix this limitation
      throw new MoxyException("Class mocking is currently only supported with the default ASMMoxyEngine");
    }

    UnsafeUtils.objectCopy(original, delegate);
    DelegateRegistry.registerDelegate(original, delegate);

    Arrays.stream(original.getClass().getDeclaredMethods()).forEach(method -> {
      if (!Modifier.isStatic(method.getModifiers())) {
        method.setAccessible(true);
        if (!method.getName().startsWith("__moxy_asm")) {
          Moxy.when(() -> method.invoke(original,
                Arrays.stream(
                    method.getParameterTypes()).map(
                        DelegateRegistry::pushTypeAppropriatePrimitiveMatcher)
                .toArray())
          ).thenCallRealMethod();
        }
      }
    });

    return delegate;
  }

  private static void setMockInstanceVariables(final ASMMoxyEngine engine, final ASMMockSupport mock) {
    try {
      final Field f = mock.getClass().getDeclaredField(TypesAndDescriptors.SUPPORT_IVARS_FIELD_NAME);

      if (f == null) {
        throw new MoxyException("[BUG] Support ivars field not found on mock");
      }

      f.setAccessible(true);

      f.set(mock, new ASMMockInstanceVars(engine));
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new MoxyException("[BUG] Unable to set mock ivars", e);
    }
  }

  private static final String PRIMITIVE_BYTE_TYPE = "byte";
  private static final String PRIMITIVE_CHAR_TYPE = "char";
  private static final String PRIMITIVE_SHORT_TYPE = "short";
  private static final String PRIMITIVE_INT_TYPE = "int";
  private static final String PRIMITIVE_LONG_TYPE = "long";
  private static final String PRIMITIVE_FLOAT_TYPE = "float";
  private static final String PRIMITIVE_DOUBLE_TYPE = "double";
  private static final String PRIMITIVE_BOOLEAN_TYPE = "boolean";

  private static Object pushTypeAppropriatePrimitiveMatcher(final Class<?> type) {
    switch (type.toString()) {
    case PRIMITIVE_BYTE_TYPE:
      return Matchers.anyByte();
    case PRIMITIVE_CHAR_TYPE:
      return Matchers.anyChar();
    case PRIMITIVE_SHORT_TYPE:
      return Matchers.anyShort();
    case PRIMITIVE_INT_TYPE:
      return Matchers.anyInt();
    case PRIMITIVE_LONG_TYPE:
      return Matchers.anyLong();
    case PRIMITIVE_FLOAT_TYPE:
      return Matchers.anyFloat();
    case PRIMITIVE_DOUBLE_TYPE:
      return Matchers.anyDouble();
    case PRIMITIVE_BOOLEAN_TYPE:
      return Matchers.anyBool();
    default:
      return Matchers.any();
    }
  }
}
