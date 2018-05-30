package com.roscopeco.moxy.matchers;

class NotEqualsMatcher<T> extends SimpleObjectMatcher<T> {  
  public NotEqualsMatcher(final T object) {
    super(object, true);
  }

  @Override
  public boolean matches(T arg) {
    T object = getObject();
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

  @Override
  public String toString() {
    return "<neq" + super.toString();
  }
}
