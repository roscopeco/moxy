package com.roscopeco.moxy.api;

import java.util.List;

/**
 * Records invocations and allows them to be queried for arguments and ordering.
 */
public interface MoxyInvocationRecorder {
  /**
   * Record the given invocation.
   */
  public void recordInvocation(Object receiver, String methodName, String methodDescriptor, Object[] args);
  
  /**
   * Remove the latest invocation from this recorder.
   */
  public void unrecordLastInvocation();
  
  /**
   * @return the map of invocations that were recorded for the given class.
   */
  // TODO this is too 'internal' to be here. Need to stop using method name/desc as key in the API.
  // Maybe if 'method' was just a single param and implementations were free to choose how it's defined...
  public List<MoxyInvocation> getInvocationList(Class<?> forClz, String methodName, String methodDesc);
  
  /**
   * Get the most recent invocation.
   */
  public MoxyInvocation getLastInvocation();
  
  /**
   * Reset this recorder.
   */
  public void reset();
}
