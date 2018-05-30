package com.roscopeco.moxy.api;

/**
 * Functional interface used to run monitored method invocations
 * (e.g. for <em>when(...)</em> and <em>assertMock(...)</em>) with
 * return type <code>T</code>.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 * 
 * @param <T> Return type of the method to be invoked.
 */
public interface InvocationSupplier<T> {
  /**
   * Run the invocation. The framework will swallow most types of exception.
   * 
   * @throws Exception Should never be thrown, just here to make the API more friendly.
   */
  public T get() throws Exception;
}
