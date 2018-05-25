package com.roscopeco.moxy.matchers;

class AnyMatcher<T> implements MoxyMatcher<T> {
  AnyMatcher() { }
  
  @Override
  public boolean matches(T arg) {
    return true;
  }

  @Override
  public String toString() {
    return "<any>";
  }  
}
