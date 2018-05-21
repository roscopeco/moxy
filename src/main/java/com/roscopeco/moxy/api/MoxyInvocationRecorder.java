package com.roscopeco.moxy.api;

import java.util.List;

/**
 * Records invocations and allows them to be queried for arguments and ordering.
 */
public interface MoxyInvocationRecorder {
  /**
   * Record the given invocation.
   */
  public void recordInvocation(Object receiver, String methodName, String methodDescriptor, List<Object> args);
  
  /**
   * Remove the latest invocation from this recorder.
   * 
   * <strong>Note:</strong> implementations should not remove the latest invocation
   * from whatever mechanism they use to store the latest invocation (i.e. the return
   * value for {@link #getLastInvocation()} when this method is called. Rather, they
   * should just remove it from their list of invocations.
   */
  // TODO these caveats about not unrecording etc. shouldn't be exposed in 
  // public API.......
  public void unrecordLastInvocation();
  
  /**
   * @return the map of invocations that were recorded for the given class.
   */
  // TODO this is too 'internal' to be here. Need to stop using method name/desc as key in the API.
  // Maybe if 'method' was just a single param and implementations were free to choose how it's defined...
  public List<MoxyInvocation> getInvocationList(Class<?> forClz, String methodName, String methodDesc);
  
  /**
   * Get the most recent invocation.
   * 
   * <strong>Note:</strong> This method must always return the most recent
   * invocation, regardless of whether it has been unrecorded (with
   * {@link #unrecordLastInvocation()}.
   */
  // TODO these caveats about not unrecording etc. shouldn't be exposed in 
  // public API.......
  public MoxyInvocation getLastInvocation();
  
  /**
   * Reset this recorder.
   */
  public void reset();
}
