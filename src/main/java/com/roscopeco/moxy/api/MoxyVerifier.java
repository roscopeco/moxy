package com.roscopeco.moxy.api;

import org.opentest4j.AssertionFailedError;

/**
 * <p>Implementations of this interface allow mocks to be verified after
 * use. They are returned by the
 * {@link MoxyEngine#assertMock(com.roscopeco.moxy.api.InvocationRunnable)} 
 * method.</p>
 * 
 * <p>Individual engines will usually provide their own implementation
 * of this interface, as verifying will require internal knowledge 
 * of the engine's mocking strategy.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyVerifier {
  /**
   * Verify that the mock method was called <em>at least</em> once,
   * with the arguments specified in the call.
   * 
   * Note the subtle difference from {@link #wasCalled(int)}, which
   * expects an <em>exact number</em> of calls.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @return <code>this</code>. 
   */
  public MoxyVerifier wasCalled();

  /**
   * Verify that the mock method was called <em>exactly</em> <code>times</code>,
   * with the arguments specified in the call.
   * 
   * Note the subtle difference from {@link #wasCalled()}, which
   * expects an <em>at least one</em> call.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @param times The number of expected invocations.
   * 
   * @return <code>this</code>. 
   */
  public MoxyVerifier wasCalled(int times);
  
  /**
   * Verify that the mock method was not called.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier wasNotCalled();
  
  /**
   * Verify the mock method was called exactly once.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier wasCalledOnce();

  /**
   * Verify the mock method called exactly twice.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier wasCalledTwice();

  /**
   * Verify the mock method was called at least <code>times</code>.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   *  
   * @param times The number of expected invocations.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier wasCalledAtLeast(int times);
  
  /**
   * Verify the mock method was called at most <code>times</code>.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @param times The number of expected invocations.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier wasCalledAtMost(int times);
  
  /**
   * Verify the mocked method never threw an exception of type <code>throwable</code>.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @param throwable The exception type to check.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier neverThrew(Class<? extends Throwable> throwable);

  /**
   * Verify the mocked method never threw the given <code>throwable</code> instance.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @param throwable The exception instance to check.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier neverThrew(Throwable throwable);
  
  /**
   * Verify the mocked method never threw any exceptions.
   * 
   * Throws {@link AssertionFailedError} if the assertion fails.
   * 
   * @return <code>this</code>
   */
  public MoxyVerifier neverThrewAnyException();
}
