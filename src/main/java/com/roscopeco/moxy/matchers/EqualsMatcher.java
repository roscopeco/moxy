package com.roscopeco.moxy.matchers;

class EqualsMatcher<T> extends SimpleObjectMatcher<T> {
  EqualsMatcher(final T object) {
    super(object, true);
  }
  
  @Override
  public boolean matches(T arg) {
    if (arg == null) {
      if (object == null) {
        return true;
      } else {
        return false;
      }
    } else {
      return arg.equals(object);
    }
  }
  
  @Override
  public String toString() {
    return "<eq" + super.toString();
  }
}
