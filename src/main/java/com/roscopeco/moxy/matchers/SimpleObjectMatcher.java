package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

abstract class SimpleObjectMatcher<T> implements MoxyMatcher<T> {
  private final T object;

  public SimpleObjectMatcher(final T object, boolean allowNull) {
    if (!allowNull && object == null) {
      throw new MoxyException("Null argument; see cause", 
          new IllegalArgumentException("Cannot match to null"));
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