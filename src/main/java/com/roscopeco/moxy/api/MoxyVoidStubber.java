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

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Special case stubber for void methods. Does not allow a return
 * value to be set.</p>
 *
 * <p>Implementations of this are returned by the
 * {@link MoxyEngine#when(com.roscopeco.moxy.api.InvocationRunnable)}
 * method</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyVoidStubber {
  /**
   * <p>Stubs the mock invocation to throw the given <code>Throwable</code>.</p>
   *
   * <p>Note that mocks may be stubbed to throw <em>any</em> throwable -
   * checked or unchecked - irrespective of the method's <code>throws</code>
   * clause. This should be used with caution as undeclared checked
   * exceptions may cause undefined behaviour in callers.</p>
   *
   * @param throwable The <code>Throwable</code> to throw for matching invocations.
   */
  public void thenThrow(Throwable throwable);

  /**
   * <p>Instead of stubbing, have the mock call the real method instead.</p>
   *
   * <p>When this method is used to have mocks call their real methods,
   * verification is unaffected. In other words, you may verify these
   * mocks in the same way as any other.</p>
   *
   * <p>For obvious reasons, this will only work where the mocked method
   * is not <code>abstract</code>. If it is, an
   * {@link InvalidStubbingException} will be thrown when the mock is invoked.</p>
   */
  public void thenCallRealMethod();

  public MoxyVoidStubber thenDo(Consumer<List<? extends Object>> action);
}
