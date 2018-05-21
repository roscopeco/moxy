package com.roscopeco.moxy.impl.asm;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.roscopeco.moxy.api.MoxyInvocation;
import com.roscopeco.moxy.api.MoxyVerifier;

import junit.framework.AssertionFailedError;

class ASMMoxyVerifier extends HasEngineAndInvocation implements MoxyVerifier {
  public ASMMoxyVerifier(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyVerifier wasCalled() {
    final MoxyInvocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    if (this.getEngine()
            .getRecorder()
            .getInvocationList(invocation.getReceiver().getClass(),
                               methodName, 
                               methodDesc)
        .stream()
        .anyMatch((e) -> Arrays.equals(e.getArgs(), invocation.getArgs()) 
    )) {
      return this;
    } else {
      throw new AssertionFailedError(
          "Expected mock " + methodName + "(...) to be called with arguments (" 
              + Arrays.stream(invocation.getArgs())
                  .map((e) -> e.toString())
                  .collect(Collectors.joining(",")) 
              + ") at least once but it wasn't");
    }
  }

  @Override
  public MoxyVerifier wasCalled(int times) {
    final MoxyInvocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    if (this.getEngine()
            .getRecorder()
            .getInvocationList(invocation.getReceiver().getClass(),
                               methodName, 
                               methodDesc)
        .stream()
        .filter( (e) -> Arrays.equals(e.getArgs(), invocation.getArgs() ))
        .collect(Collectors.toList())
        .size() == times
    ) {
      return this;
    } else {
      throw new AssertionFailedError(
          "Expected mock " + methodName + "(...) to be called with arguments (" 
              + Arrays.stream(invocation.getArgs())
                  .map((e) -> e.toString())
                  .collect(Collectors.joining(",")) 
              + ") exactly " + times + " time(s) but it wasn't");
    }
  }
}
