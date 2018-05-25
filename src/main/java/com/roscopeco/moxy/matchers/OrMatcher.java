package com.roscopeco.moxy.matchers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

class OrMatcher<T> implements MoxyMatcher<T> {
  // This is only used to determine how many matchers we should be popping
  final Object[] passedArgs;

  private final List<MoxyMatcher<? super T>> matchers = new ArrayList<>();
  
  @SafeVarargs
  OrMatcher(final T... objects) {
    this.passedArgs = objects;
  }
  
  public List<MoxyMatcher<? super T>> getMatchers() {
    return matchers;
  }

  @Override
  public boolean matches(T arg) {
    return matchers.stream().anyMatch((e) -> e.matches(arg));
  }
  
  // TODO not strictly type-safe, but only used internally so it'll be fine...
  ///         ... as long as long as the stack stays consistent (!)
  //
  // TODO the right way to do this would probably be to just get rid of the generics...?
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void addToStack(Deque<MoxyMatcher<?>> stack) {
    int numMatchers = passedArgs.length;
    
    if (stack.size() < numMatchers) {
      throw new IllegalMatcherStateException("Not enough matchers for or(...) (Expected " 
          + numMatchers + ")"
          + "Ensure you're passing another matcher to or(...)");       
          
    } else {
      ArrayDeque<MoxyMatcher<? super T>> deque = new ArrayDeque<>();
      for (int i = 0; i < numMatchers; i++) {
        deque.push((MoxyMatcher)stack.pop());
      }
      
      this.matchers.addAll(deque);
      stack.push(this);      
    }
  }

  @Override
  public String toString() {
    if (this.matchers.size() < 3) {
      return "<or: " 
          + this.matchers.stream().map(Object::toString).collect(Collectors.joining(", "))
          + ">";
    } else {
      return "<or: ...>";
    }    
  }
}
