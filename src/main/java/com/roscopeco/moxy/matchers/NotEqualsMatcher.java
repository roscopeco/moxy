package com.roscopeco.moxy.matchers;

public class NotEqualsMatcher<T> extends SimpleObjectMatcher<T> {  
  public NotEqualsMatcher(final T object) {
    super(object, true);
  }

  @Override
  public boolean matches(T arg) {
    if (arg == null) {
      if (object == null) {
        return false;
      } else {
        return true;
      }
    } else {
      return !arg.equals(object);
    }
  }
}
