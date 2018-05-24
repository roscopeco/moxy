package com.roscopeco.moxy.matchers;

public class AnyMatcher<T> implements MoxyMatcher<T> {
  @Override
  public boolean matches(T arg) {
    return true;
  }
  
  @Override
  public String toString() {
    return "<any>";
  }  
}
