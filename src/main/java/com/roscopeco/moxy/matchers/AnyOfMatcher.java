package com.roscopeco.moxy.matchers;

import java.util.List;
import java.util.stream.Collectors;

class AnyOfMatcher<T> implements MoxyMatcher<T> {
  private final List<? extends T> objects;
  
  AnyOfMatcher(final List<? extends T> objects) {
    if (objects == null) {
      // fail fast
      throw new NullPointerException("Cannot match to null list - try an empty list (or eq(null)) instead");
    }
    
    this.objects = objects;    
  }
  
  public List<? extends T> getObjects() {
    return objects;
  }

  @Override
  public boolean matches(T arg) {
    return objects.contains(arg);
  }

  @Override
  public String toString() {
    if (this.objects.size() < 3) {
      return "<anyOf: " 
          + this.objects.stream().map(Object::toString).collect(Collectors.joining(", "))
          + ">";
    } else {
      return "<anyOf: ...>";
    }    
  }
}
