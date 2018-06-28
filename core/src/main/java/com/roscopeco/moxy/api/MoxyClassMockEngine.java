/*
 * MoxyClassMockEngine.java -
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

/**
 * <p>An engine used to create Moxy class mocks.</p>
 *
 * <p>Note that this is very different to the standard
 * mocking performed by {@link MoxyEngine}s. This style of mocking
 * uses runtime instrumentation to transform the supplied classes
 * directly, instead of, for example, creating proxy classes.</p>
 *
 * <p>For example, with class mocks, the following is possible:</p>
 *
 * <pre><code>
 * Moxy.classMocks(MyClass.class);    // MyClass may or may not be final...
 *
 * // ... later
 *
 * MyClass myClass = new MyClass();
 *
 * // ... myClass is a mock
 *
 * assertTrue(Moxy.isMock(myClass));
 *
 * Moxy.when(() -> myClass.someMethod("args")).thenReturn("Mocked!");
 *
 * assertThat(myClass.someMethod("args")).isEqualTo("Mocked!");
 * </code></pre>
 *
 * <p>This style of mocking allows you to do things that
 * cannot be achieved with standard mocks:</p>
 *
 * <ul>
 * <li>Final classes and methods can be mocked.</li>
 * <li>Constructors become mocks, so can be verified. &#x1f7bd;</li>
 * <li>Static methods become mocks. &#x1f7bd;</li>
 * <li>Objects allocated directly inside tested methods (with <code>new</code>)
 *     will be mocks, and so can be verified. &#x1f7bd;</li>
 * </ul>
 *
 * <p><strong>Note:</strong> items marked &#x1f7bd;, above, are currently
 * unsupported by the rest of the framework. Work is in-progress to
 * implement these features.</p>
 *
 * <p>There are, however, some significant caveats to be aware of:</p>
 *
 * <ul>
 * <li>The transformation will affect <em>existing</em> instances of the
 *     mocked classes, meaning any instances in use within the running
 *     JVM will also become mocks.<br>
 *     <br>
 *     Work is currently on-going to make this less of a problem, however
 *     at present any such instances will cause an exception to be thrown
 *     when they are next invoked.</li>
 * <li>
 * <li>This style of mocking requires a Java Agent to be installed in
 *     the running JVM. Not all JVMs support this, although it should
 *     work on all those targeted by Moxy.</li>
 * <li>This style of mocking raises significant security and safety
 *     issues, and should never be used in production. But you're not
 *     running tests in production code anyway, right?
 * </ul>
 *
 * <p>This feature is highly experimental, use it at your own risk.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyClassMockEngine {

  /**
   * <p>Convert the given classes to mock classes.</p>
   *
   * <p>when this method returns, all instances of the given classes will
   * be converted to mocks, along with all future instances.</p>
   *
   * @param classes Classes to convert.
   * @since 1.0
   */
  public void mockClasses(Class<?>... classes);

  /**
   * <p>Reset the given classes to their original, non-mock implementation.</p>
   *
   * @param classes Classes to convert.
   *
   * @see #mockClasses(Class...)
   * @since 1.0
   */
  public void resetClasses(Class<?>... classes);

  /**
   * <p>Reset the given classes to their original, non-mock implementation.</p>
   *
   * @see #mockClasses(Class...)
   * @see #resetClasses(Class...)
   * @since 1.0
   */
  public void resetAllClasses();
}
