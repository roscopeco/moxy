package com.roscopeco.moxy.matchers;

import java.util.Deque;

public interface MoxyMatcher<T> {
  public boolean matches(T arg);
  
  // TODO don't like this, it exposes impl details.
  //      Maybe matchers should be made internal, and the engine handles it all.
  default public void addToStack(Deque<MoxyMatcher<?>> stack) {
    stack.push(this);    
  }
}
