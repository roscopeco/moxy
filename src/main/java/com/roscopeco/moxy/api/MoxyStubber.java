package com.roscopeco.moxy.api;

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
   * @return <code>this</code>
   */
  public MoxyStubber<T> thenReturn(T object);
  
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
  public MoxyStubber<T> thenThrow(Throwable throwable);
  
  public MoxyStubber<T> thenCallRealMethod();
}
