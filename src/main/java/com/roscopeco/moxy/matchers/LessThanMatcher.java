package com.roscopeco.moxy.matchers;

public class LessThanMatcher<T extends Comparable<T>> extends SimpleObjectMatcher<T> {
  LessThanMatcher(final T object) {
    super(object, false);
  }
  
  @Override
  public boolean matches(T arg) {
    // NOTE check reversed for easier null handling
    return object.compareTo(arg) > 0;
  }
  
  @Override
  public String toString() {
    return "<lt" + super.toString();
  }
}
