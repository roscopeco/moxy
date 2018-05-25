package com.roscopeco.moxy.matchers;

import java.util.Deque;

public class AnyMatcher<T> implements MoxyMatcher<T> {
  AnyMatcher() { }
  
  @Override
  public boolean matches(T arg) {
    return true;
  }
  
  @Override
  public void addToStack(Deque<MoxyMatcher<?>> stack) {
    stack.push(this);
  }

  @Override
  public String toString() {
    return "<any>";
  }  
}
