package com.roscopeco.moxy.matchers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

class AndMatcher<T> implements MoxyMatcher<T> {
  // This is only used to determine how many matchers we should be popping
  final Object[] passedArgs;
  
  private final ArrayList<MoxyMatcher<? super T>> matchers = new ArrayList<>();
  
  @SafeVarargs
  AndMatcher(final T... objects) {
    this.passedArgs = objects;
  }
  
  public List<MoxyMatcher<? super T>> getMatchers() {
    return this.matchers;
  }

  @Override
  public boolean matches(T arg) {
    return this.matchers.stream().allMatch((e) -> e.matches(arg));
  }

  // TODO inefficient way to reverse the matchers!
  // TODO not strictly type-safe, but only used internally so it'll be fine...
  ///         ... as long as long as the stack stays consistent (!)
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void addToStack(Deque<MoxyMatcher<?>> stack) {
    int numMatchers = passedArgs.length;
    
    if (stack.size() < numMatchers) {
      throw new IllegalMatcherStateException("Not enough matchers for and(...) (Expected " 
          + numMatchers + ")\n"
          + "Ensure you're passing another matcher to and(...)");       
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
      return "<and: " 
          + this.matchers.stream().map(Object::toString).collect(Collectors.joining(", "))
          + ">";
    } else {
      return "<and: ...>";
    }    
  }
}
