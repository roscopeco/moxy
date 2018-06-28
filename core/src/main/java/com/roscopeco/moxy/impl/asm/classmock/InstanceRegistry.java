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

import com.roscopeco.moxy.impl.asm.ASMMockSupport;

/**
 * TODO Document InstanceRegistry
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class InstanceRegistry {
  private static final Logger LOG = Logger.getLogger(InstanceRegistry.class.getName());

  private static final WeakHashMap<Object, ASMMockSupport> registryMap = new WeakHashMap<>();

  public static void registerDelegate(final Object forObj, final ASMMockSupport delegate) {
    final Object old = registryMap.put(forObj, delegate);

    if (old != null && old != delegate) {
      LOG.warning(() -> "Delegate changed for object '" + forObj + "' (from '" + old + "' to '" + delegate + "'");
    }
  }

  public static ASMMockSupport getDelegate(final Object forObj) {
    return registryMap.get(forObj);
  }
}
