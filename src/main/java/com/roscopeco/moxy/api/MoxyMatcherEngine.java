package com.roscopeco.moxy.api;

import com.roscopeco.moxy.matchers.MoxyMatcher;

public interface MoxyMatcherEngine {
  public void registerMatcher(MoxyMatcher<?> matcher);
}
