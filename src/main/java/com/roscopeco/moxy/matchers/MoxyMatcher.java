package com.roscopeco.moxy.matchers;

public interface MoxyMatcher<T> {
  public boolean matches(T arg);
}
