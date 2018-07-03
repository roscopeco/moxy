/*
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

package com.roscopeco.moxy.impl.asm.classmock;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.roscopeco.moxy.api.MoxyClassMockEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.impl.asm.UnsafeUtils;
import com.roscopeco.moxy.impl.asm.visitors.classmock.MoxyClassMockAdapter;
import com.roscopeco.moxy.impl.asm.visitors.classmock.MoxyClassMockDelegateAdapter;

import net.bytebuddy.agent.ByteBuddyAgent;

/*
 * MoxyClassMockEngine using ASM.
 */
public class ASMClassMockEngine implements MoxyClassMockEngine, ClassFileTransformer {
  // set to "true" to debug all, or to a class name to debug a single class
  private static final String DEBUG_CLASSMOCK_PROPERTY = "com.roscopeco.moxy.classmock.debug";

  private static Instrumentation instrumentation;

  private static synchronized Instrumentation instrumentation() {
    if (instrumentation == null) {
      try {
        instrumentation = ByteBuddyAgent.install();
      } catch (final IllegalStateException e) {
        throw new MoxyException("Unable to install agent into this VM; Class mock not supported");
      }
    }

    return instrumentation;
  }

  private final HashSet<Class<?>> pendingMock = new HashSet<>();
  private final HashSet<Class<?>> pendingReset = new HashSet<>();
  private final HashSet<Class<?>> currentlyMockedClasses = new HashSet<>();

  public ASMClassMockEngine() {
    instrumentation().addTransformer(this, true);
  }

  @Override
  protected void finalize() throws Throwable {
    instrumentation().removeTransformer(this);
    super.finalize();
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyClassMockEngine#mockClasses(java.lang.Class[])
   */
  @Override
  public void mockClasses(final Class<?>... classes) {
    for (final Class<?> clz : classes) {
      this.addPendingMock(clz);
    }

    try {
      instrumentation().retransformClasses(classes);
    } catch (final Exception e) {
      throw new MoxyException("Unable to mock one or more classes. See cause", e);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyClassMockEngine#resetClasses(java.lang.Class[])
   */
  @Override
  public void resetClasses(final Class<?>... classes) {
    for (final Class<?> clz : classes) {
      this.addPendingReset(clz);
    }

    try {
      instrumentation().retransformClasses(classes);
    } catch (final Exception e) {
      throw new MoxyException("Unable to reset one or more classes. See cause", e);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyClassMockEngine#resetAllClasses()
   */
  @Override
  public void resetAllClasses() {
    synchronized (this.currentlyMockedClasses) {
      if (this.currentlyMockedClasses.size() > 0) {
        this.resetClasses(this.currentlyMockedClasses.toArray(new Class<?>[this.currentlyMockedClasses.size()]));
      }
    }
  }

  private void addPendingMock(final Class<?> clz) {
    synchronized(this.pendingMock) {
      this.pendingMock.add(clz);
    }
  }

  private boolean isPendingMock(final Class<?> clz) {
    synchronized(this.pendingMock) {
      return this.pendingMock.remove(clz);
    }
  }

  private void addPendingReset(final Class<?> clz) {
    synchronized(this.pendingReset) {
      this.pendingReset.add(clz);
    }
  }

  private boolean isPendingReset(final Class<?> clz) {
    synchronized(this.pendingReset) {
      return this.pendingReset.remove(clz);
    }
  }

  Class<?> copyClass(final ClassLoader loader, final Class<?> originalClz, final byte[] original) {
    final ClassReader reader = new ClassReader(original);

    final ClassNode node = new ClassNode();
    final MoxyClassMockDelegateAdapter delegateAdapter =
        new MoxyClassMockDelegateAdapter(node, originalClz);

    reader.accept(delegateAdapter, ClassReader.SKIP_DEBUG);

    final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

    if ("true".equals(System.getProperty(DEBUG_CLASSMOCK_PROPERTY)) ||
        originalClz.getName().equals(System.getProperty(DEBUG_CLASSMOCK_PROPERTY))) {
      final CheckClassAdapter check = new CheckClassAdapter(writer, false);
      final TraceClassVisitor traceVisitor = new TraceClassVisitor(check, new PrintWriter(System.out));
      node.accept(traceVisitor);
    } else {
      node.accept(writer);
    }

    DelegateRegistry.registerDelegateClass(originalClz, delegateAdapter.getNewJavaName());
    return UnsafeUtils.defineClass(loader, delegateAdapter.getNewJavaName(), writer.toByteArray());
  }

  @Override
  public byte[] transform(final ClassLoader loader, final String name, final Class<?> originalClz,
      final ProtectionDomain pd, final byte[] originalCode) throws IllegalClassFormatException {
    if (originalClz != null) {
      if (this.isPendingReset(originalClz)) {
        // Remove from mocked classes
        this.currentlyMockedClasses.remove(originalClz);

        // clear registered delegate class
        //DelegateRegistry.removeDelegateClass(originalClz);

        // clear static delegate (if any)
        DelegateRegistry.clearStaticDelegate(originalClz);

        // return original code
        return originalCode;
      } else if (this.isPendingMock(originalClz)) {
        try {
          final Class<?> copy = this.copyClass(loader, originalClz, originalCode);

          ClassReader reader = new ClassReader(originalCode);
          final ClassNode node = new ClassNode();
          final MoxyClassMockAdapter adapter = new MoxyClassMockAdapter(node, originalClz, copy);
          reader.accept(adapter, 0);

          ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
          node.accept(writer);
          final byte[] newCode = writer.toByteArray();

          if ("true".equals(System.getProperty(DEBUG_CLASSMOCK_PROPERTY)) ||
              originalClz.getName().equals(System.getProperty(DEBUG_CLASSMOCK_PROPERTY))) {
            reader = new ClassReader(newCode);
            writer = new ClassWriter(0);
            final CheckClassAdapter check = new CheckClassAdapter(writer);
            final TraceClassVisitor trace = new TraceClassVisitor(check, new PrintWriter(System.out));
            reader.accept(trace, 0);
          }

          this.currentlyMockedClasses.add(originalClz);
          return newCode;
        } catch (final Throwable t) {
          System.err.println("Exception in transform: " + t);
          t.printStackTrace();
        }
      }
    }

    // No transform by default
    return null;
  }
}
