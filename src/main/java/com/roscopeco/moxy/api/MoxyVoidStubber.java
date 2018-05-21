package com.roscopeco.moxy.api;

/**
 * Special case stubber for void methods. Does not allow a return
 * value to be set.
 * 
 * @author Ross.Bamford
 */
public interface MoxyVoidStubber {
  public MoxyVoidStubber thenThrow(Throwable throwable);
}
