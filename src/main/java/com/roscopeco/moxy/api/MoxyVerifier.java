package com.roscopeco.moxy.api;

import junit.framework.AssertionFailedError;

public interface MoxyVerifier<T> {
  /**
   * Verify that the mock method was called <em>at least</em> once,
   * with the arguments specified in the call.
   * 
   * Note the subtle difference from {@link #wasCalled(int)}, which
   * expects an <em>exact number</em> of calls.
   * 
   * @return <code>this</code>. 
   * @throws AssertionFailedError if the assertion fails.
   */
  public MoxyVerifier<T> wasCalled();

  /**
   * Verify that the mock method was called <em>exactly</em> <code>times</code>,
   * with the arguments specified in the call.
   * 
   * Note the subtle difference from {@link #wasCalled()}, which
   * expects an <em>at least one</em> call.
   * 
   * @return <code>this</code>. 
   * @throws AssertionFailedError if the assertion fails.
   */
  public MoxyVerifier<T> wasCalled(int times);
}
