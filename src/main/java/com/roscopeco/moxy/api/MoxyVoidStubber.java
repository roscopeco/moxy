package com.roscopeco.moxy.api;

/**
 * <p>Special case stubber for void methods. Does not allow a return
 * value to be set.</p>
 * 
 * <p>Implementations of this are returned by the {@link MoxyEngine#when(Runnable)}
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
   * 
   * @return <code>this</code>
   */  
  public MoxyVoidStubber thenThrow(Throwable throwable);
}
