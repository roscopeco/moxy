package com.roscopeco.moxy.matchers;

abstract class SimpleObjectMatcher<T> implements MoxyMatcher<T> {
  private final T object;

  public SimpleObjectMatcher(final T object, boolean allowNull) {
    if (!allowNull && object == null) {
      throw new IllegalArgumentException("Cannot match to null");
    }
    
    this.object = object;
  }

  protected T getObject() {
    return object;
  }

  @Override
  public String toString() {
    if (this.object == null) {
      return ": null>";
    } else {
      return ": " + object.toString() + ">";
    }
  }
}