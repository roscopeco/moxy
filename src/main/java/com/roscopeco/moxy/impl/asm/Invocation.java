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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.roscopeco.moxy.api.MoxyException;

/**
 * Represents a single invocation of a given method, on a given receiver,
 * with given arguments.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 *
 */
final class Invocation {
  private static final List<Object> EMPTY_OBJECT_LIST = Collections.emptyList();

  private final Object receiver;
  private final String methodName;
  private final String methodDesc;
  private final List<Object> args;
  private Object returned;
  private Throwable threw;

  /**
   * Create a new Invocation with the specified receiver, method and arguments.
   *
   * @param receiver
   * @param methodName
   * @param methodDesc
   * @param args
   */
  public Invocation(final Object receiver,
                    final String methodName,
                    final String methodDesc,
                    final List<Object> args) {
    if (receiver == null ||
        methodName == null ||
        methodName.isEmpty() ||
        methodDesc == null ||
        methodDesc.isEmpty()) {
      throw new MoxyException("Illegal argument: Invocation.<init>(...). See cause.",
          new IllegalArgumentException("Cannot create invocation: receiver and/or methodName/methodSig are null (or empty)"));
    }

    this.receiver = receiver;
    this.methodName = methodName;
    this.methodDesc = methodDesc;
    this.args = args;
  }

  /**
   * @return the receiver.
   */
  public Object getReceiver() {
    return this.receiver;
  }

  /**
   * @return the invoked method name.
   */
  public String getMethodName() {
    return this.methodName;
  }

  /**
   * @return the invoked method's descriptor;
   */
  public String getMethodDesc() {
    return this.methodDesc;
  }

  /**
   * @return the arguments the method was called with. Possibly empty, never null.
   */
  public List<Object> getArgs() {
    return this.args == null ? EMPTY_OBJECT_LIST : this.args;
  }

  /**
   * @return The object this method invocation returned (may be null).
   */
  Object getReturned() {
    return this.returned;
  }

  void setReturned(final Object returned) {
    this.returned = returned;
  }

  /*
   * Returns a human-readble string representation of this invocation.
   */
  @Override
  public String toString() {
    return this.getMethodName() + "(" + TypeStringUtils.inspectArgs(this) + ")";
  }

  /**
   *
   * @return The exception this method invocation threw (may be null).
   */
  Throwable getThrew() {
    return this.threw;
  }

  void setThrew(final Throwable threw) {
    this.threw = threw;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.receiver, this.methodName, this.methodDesc, this.args);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Invocation other = (Invocation) obj;
    if (this.args == null) {
      if (other.args != null) {
        return false;
      }
    } else if (!this.args.equals(other.args)) {
      return false;
    }

    if (this.methodDesc == null) {
      if (other.methodDesc != null) {
        return false;
      }
    } else if (!this.methodDesc.equals(other.methodDesc)) {
      return false;
    }
    if (this.methodName == null) {
      if (other.methodName != null) {
        return false;
      }
    } else if (!this.methodName.equals(other.methodName)) {
      return false;
    }
    if (this.receiver == null) {
      if (other.receiver != null) {
        return false;
      }
    } else if (!this.receiver.equals(other.receiver)) {
      return false;
    }
    return true;
  }
}
