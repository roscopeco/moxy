package com.roscopeco.moxy.impl.asm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.roscopeco.moxy.api.MoxyMatcherEngine;
import com.roscopeco.moxy.matchers.MoxyMatcher;

class ASMMoxyMatcherEngine implements MoxyMatcherEngine {
  ThreadLocal<ArrayDeque<MoxyMatcher<?>>> matcherStack = new ThreadLocal<>();

  ArrayDeque<MoxyMatcher<?>> ensureMatcherStack() {
    ArrayDeque<MoxyMatcher<?>> stack = matcherStack.get();
    
    if (stack == null) {
      matcherStack.set(stack = new ArrayDeque<>());      
    }
    
    return stack;
  }
  
  @Override
  public void registerMatcher(MoxyMatcher<?> matcher) {
    ensureMatcherStack().push(matcher);
  }
  
  List<MoxyMatcher<?>> popMatchers() {
    ArrayDeque<MoxyMatcher<?>> stack = ensureMatcherStack();
    if (stack.isEmpty()) {
      return null;
    } else {
      ArrayList<MoxyMatcher<?>> result = new ArrayList<>();
      while (!stack.isEmpty()) {
        result.add(stack.pop());
      }
      return result;
    }    
  }
}
