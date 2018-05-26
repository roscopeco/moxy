package com.roscopeco.moxy.impl.asm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.roscopeco.moxy.api.MoxyMatcherEngine;
import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.matchers.MoxyMatcher;

class ASMMoxyMatcherEngine implements MoxyMatcherEngine {
  final ASMMoxyEngine engine;
  
  ASMMoxyMatcherEngine(ASMMoxyEngine engine) {
    this.engine = engine;
  }
  
  ThreadLocal<ArrayDeque<MoxyMatcher<?>>> matcherStack = new ThreadLocal<>();
  
  ASMMoxyEngine getASMMoxyEngine() {
    return this.engine;
  }

  ArrayDeque<MoxyMatcher<?>> ensureMatcherStack() {
    ArrayDeque<MoxyMatcher<?>> stack = matcherStack.get();
    
    if (stack == null) {
      matcherStack.set(stack = new ArrayDeque<>());      
    }
    
    return stack;
  }
  
  @Override
  public void registerMatcher(MoxyMatcher<?> matcher) {
    matcher.addToStack(ensureMatcherStack());
  }
  
  List<MoxyMatcher<?>> popMatchers() {
    ArrayDeque<MoxyMatcher<?>> stack = ensureMatcherStack();
    if (stack.isEmpty()) {
      return null;
    } else {
      ArrayList<MoxyMatcher<?>> result = new ArrayList<>();
      while (!stack.isEmpty()) {
        result.add(stack.removeLast());
      }
      return result;
    }    
  }
  
  // suppress because we check manually
  @SuppressWarnings("unchecked")
  boolean argsMatch(List<Object> actualArgs, List<Object> storedArgs) {
    if (storedArgs.size() != actualArgs.size()) {
      return false;
    }
    
    boolean result = true;
    
    for (int i = 0; i < storedArgs.size(); i++) {
      Object stored = storedArgs.get(i);
      Object actual = actualArgs.get(i);
      
      if (stored instanceof MoxyMatcher) {
        MoxyMatcher<Object> matcher = (MoxyMatcher<Object>)stored;
        if (!matcher.matches(actual)) {
          result = false;
        }        
      } else {
        if (!stored.equals(actual)) {
          result = false;
        }
      }
    }
    
    return result;
  }
  
  /*
   * Verifies the stack is empty. Called at entry and exit to the
   * framework (i.e. start and end of when() and assert() calls).
   * 
   * If non-empty, throws InconsistentMatchersException.
   */
  void validateStackConsistency() {
    if (!(this.matcherStack.get() == null || this.matcherStack.get().isEmpty())) {
      throw new InconsistentMatchersException(0, this.matcherStack.get());      
    }    
  }
}
