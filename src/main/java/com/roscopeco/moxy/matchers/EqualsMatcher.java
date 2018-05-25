package com.roscopeco.moxy.matchers;

public class EqualsMatcher<T> implements MoxyMatcher<T> {
  private final T object;
  
  EqualsMatcher(final T object) {
    this.object = object;
  }
  
  public T getObject() {
    return object;
  }
  
  @Override
  public boolean matches(T arg) {
    return object.equals(arg);
  }
  
  @Override
  public String toString() {
    if (this.object == null) {
      return "null";
    } else {
      return object.toString();
    }
  }
}
