package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

class InstanceOfMatcher<T> implements MoxyMatcher<T> {
  private final Class<T> clz;

  public InstanceOfMatcher(final Class<T> clz) {
    if (clz == null) {
      throw new MoxyException("Null argument; see cause",
          new IllegalArgumentException("Cannot match to null"));
    }

    this.clz = clz;
  }

  @Override
  public boolean matches(final T arg) {
    if (arg != null) {
      return this.clz.isAssignableFrom(arg.getClass());
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return "<instanceOf: " + this.clz + ">";
  }
}
