package com.roscopeco.moxy.api;

/**
 * Functional interface used to run monitored void method invocations
 * (e.g. for <em>when(...)</em> and <em>assertMock(...)</em>).
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface InvocationRunnable {
  /**
   * Run the invocation. The framework will swallow most types of exception.
   * 
   * @throws Exception Should never be thrown, just here to make the API more friendly.
   */
  public void run() throws Exception;
}
