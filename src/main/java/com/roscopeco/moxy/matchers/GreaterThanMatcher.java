package com.roscopeco.moxy.matchers;

public class GreaterThanMatcher<T extends Comparable<T>> extends SimpleObjectMatcher<T> {
  GreaterThanMatcher(final T object) {
    super(object, false);
  }
  
  @Override
  public boolean matches(T arg) {
    // NOTE check reversed for easier null handling
    return object.compareTo(arg) < 0;
  }
  
  @Override
  public String toString() {
    return "<gt" + super.toString();
  }  
}
