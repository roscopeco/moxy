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
 * <p>Implementations of this interface allow mocks to be stubbed to
 * throw or return given values. They are returned by the
 * {@link MoxyEngine#when(com.roscopeco.moxy.api.InvocationSupplier)} method.</p>
 *
 * <p>Individual engines will usually provide their own implementation
 * of this interface, as stubbing will require internal knowledge
 * of the engine's mocking strategy.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 *
 * @param <T> The type this stubber handles.
 */
public interface MoxyStubber<T> {
  /**
   * <p>Stubs the mock invocation to return the given <code>Object</code>.</p>
   *
   * <p>Where the method being stubbed is primitive, the appropriate
   * primitive wrapper type should be returned, which will then be
   * boxed/unboxed as required by the framework.</p>
   *
   * @param object The <code>Object</code> to return for matching invocations.
   *
   * @since 1.0
   */
  public void thenReturn(T object);

  /**
   * <p>Stubs the mock invocation to throw the given <code>Throwable</code>.</p>
   *
   * <p>Note that mocks may be stubbed to throw <em>any</em> throwable -
   * checked or unchecked - irrespective of the method's <code>throws</code>
   * clause. This should be used with caution as undeclared checked
   * exceptions may cause undefined behaviour in callers.</p>
   *
   * @param throwable The <code>Throwable</code> to throw for matching invocations.
   *
   * @since 1.0
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
   *
   * @since 1.0
   */
  public void thenCallRealMethod();

  /**
   * <p>Stub this method to return the value calculated by the supplied
   * {@link AnswerProvider}.</p>
   *
   * <p>The provider receives the actual arguments the method was
   * called with as an immutable <code>List</code>.</p>
   *
   * <p>For example:</p>
   *
   * <pre><code>
   *   Moxy.when(() -&gt; mock.something("arg")).thenAnswer(args -&gt; args.get(0));
   * </code></pre>
   *
   * @param provider The {@link AnswerProvider}, usually as a lambda.
   */
  public void thenAnswer(AnswerProvider<T> provider);

  /**
   * <p>Add a <em>doAction</em> to this method.</p>
   *
   * <p><em>doActions</em> are arbitrary actions that are executed, in order,
   * during invocation of mocked methods. These actions can be applied to
   * any mock, including those that use {@link #thenCallRealMethod()}.</p>
   *
   * <p>The action receives the actual arguments the method was
   * called with as an immutable <code>List</code>.</p>
   *
   * <p>It is possible to chain multiple actions on a single invocation,
   * for example:</p>
   *
   * <pre><code>
   *   Moxy.when(() -&gt; mock.something("arg"))
   *       .thenDo(args -&gt; System.out.println("mock.something called"))
   *       .thenDo(args -&gt; customRecorder.record("something", args))
   *       .thenCallRealMethod();
   * </code></pre>
   *
   * <p><strong>Note:</strong> For a given invocation, all matching <em>doActions</em>
   * will be invoked, even if they weren't (intentionally) applied to the
   * given arguments. This only applies when argument matchers are used.</p>
   *
   * <p>As an example of this potentially-confusing behaviour, consider the
   * following example:</p>
   *
   * <pre><code>
   *   Moxy.when(() -&gt; mock.hasTwoArgs(Matchers.any(), Matchers.eqInt(5)))
   *     .thenDo(() -&gt; System.out.println("Action 1"));
   *     .thenDo(() -&gt; System.out.println("Action 2"));
   *
   *   Moxy.when(() -&gt; mock.hasTwoArgs(Matchers.eq("Bill"), Matchers.anyInt()))
   *     .thenDo(() -&gt; System.out.println("Action 3"));
   *
   *   mock.hasTwoArgs("Bill", 5);
   * </code></pre>
   *
   * <p>In this example, the invocation <code>mock.hasTwoArgs("Bill", 5)</code> matches
   * <em>both</em> stubbed invocations, so all actions will run, resulting in the
   * following output:</p>
   *
   * <pre><code>
   *   Action 1
   *   Action 2
   *   Action 3
   * </code></pre>
   *
   * <p>I.e., since the framework has no way to tell exactly which of the two
   * potential matching preconditions you actually wanted, it calls them both.</p>
   *
   * <p>For this reason, you should exercise caution when using matchers with
   * <em>doActions</em>, to ensure you only execute actions on the invocations
   * you actually want to invoke them on. This is especially true where your
   * actions have side effects.</p>
   *
   * @since 1.0
   * @param action The action, usually as a lambda.
   *
   * @return <code>this</code>, for continued stubbing.
   */
  public MoxyStubber<T> thenDo(Consumer<List<? extends Object>> action);
}
