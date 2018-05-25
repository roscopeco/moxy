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
  public void registerMatcher(MoxyMatcher<?> matcher);
}
