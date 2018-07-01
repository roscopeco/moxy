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

import java.util.WeakHashMap;
import java.util.logging.Logger;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.impl.asm.ASMMockSupport;
import com.roscopeco.moxy.impl.asm.ASMMoxyEngine;

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

  private static final WeakHashMap<Object, ASMMockSupport> instanceRegistryMap = new WeakHashMap<>();
  private static final WeakHashMap<Class<?>, ASMMockSupport> staticRegistryMap = new WeakHashMap<>();

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
      return instanceRegistryMap.get(forObj);
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
}
