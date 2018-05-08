package com.roscopeco.moxy.api;

/**
 * Records invocations and allows them to be queried for arguments and ordering.
 */
public interface MoxyInvocationRecorder {
  /**
   * Record the given invocation.
   */
  public void recordInvocation(Object receiver, String methodName, String methodDescriptor, Object[] args);
  
  /**
   * Get the most recent invocation.
   */
  public MoxyInvocation getLastInvocation();
}
