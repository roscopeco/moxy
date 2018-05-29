package com.roscopeco.moxy.api;

import com.roscopeco.moxy.matchers.MoxyMatcher;

/**
 * <p>Implementations of this interface integrate the {@link MoxyMatcher}
 * API with the {@link com.roscopeco.moxy.api.MoxyEngine}.</p>
 * 
 * <p>Implementations will usually be provided by the engine directly,
 * since they will require fairly tight integration.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public interface MoxyMatcherEngine {
  /**
   * <p>Register the given matcher with this <code>MoxyMatcherEngine</code>.</p>
   * 
   * <p>This method triggers a two-step process, whereby the matcher engine
   * takes care of any housekeeping required by the current {@link MoxyEngine}
   * and then calls back to the {@link MoxyMatcher#addToStack(java.util.Deque)}
   * method, passing in the appropriate stack.</p>
   * 
   * <p>See the documentation on {@link MoxyMatcher} for more information.</p>
   * 
   * @param matcher The {@link MoxyMatcher} to register.
   */
  public void registerMatcher(MoxyMatcher<?> matcher);
}
