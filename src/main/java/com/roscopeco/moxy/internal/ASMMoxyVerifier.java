package com.roscopeco.moxy.internal;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.roscopeco.moxy.api.MoxyInvocation;
import com.roscopeco.moxy.api.MoxyVerifier;

import junit.framework.AssertionFailedError;

class ASMMoxyVerifier<T> extends HasEngineAndInvocation implements MoxyVerifier<T> {
  public ASMMoxyVerifier(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyVerifier<T> wasCalled() {
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
      throw new AssertionFailedError("Expected mock " + methodName + "(...) to be called at least once but it wasn't");
    }
  }

  @Override
  public MoxyVerifier<T> wasCalled(int times) {
    return this;
  }
}
