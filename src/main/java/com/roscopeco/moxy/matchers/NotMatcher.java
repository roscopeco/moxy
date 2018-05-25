package com.roscopeco.moxy.matchers;

import java.util.Deque;

public class NotMatcher<T> implements MoxyMatcher<T> {
  private MoxyMatcher<? super T> matcher;
  
  public MoxyMatcher<? super T> getMatcher() {
    return matcher;
  }

  @Override
  public boolean matches(T arg) {
    return !matcher.matches(arg);
  }

  // TODO not strictly type-safe, but only used internally so it'll be fine...
  ///         ... as long as long as the stack stays consistent (!)
  @SuppressWarnings("unchecked")
  @Override
  public void addToStack(Deque<MoxyMatcher<?>> stack) {
    if (stack.size() < 1) {
      throw new IllegalMatcherStateException("Not enough matchers for not(...) (Expected 1)\n"
                                           + "Ensure you're passing another matcher to not(...)");       
    } else {
      this.matcher = (MoxyMatcher<? super T>)stack.pop();
      stack.push(this);
    }    
  }
  
  @Override
  public String toString() {
    return "<not: " + this.matcher.toString() + ">";
  }
}
