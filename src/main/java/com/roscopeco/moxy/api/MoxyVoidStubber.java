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
  public MoxyVoidStubber thenThrow(Throwable throwable);
}
