package com.roscopeco.moxy.internal;

import com.roscopeco.moxy.api.Invocation;
import com.roscopeco.moxy.api.Stubber;

class ASMStubber<T> implements Stubber<T> {
  private final ASMMoxyEngine engine;
  private final Invocation theInvocation;
  
  public ASMStubber(ASMMoxyEngine engine) {
    this.engine = engine;
    this.theInvocation = engine.getRecorder().getLastInvocation();

    if (this.theInvocation == null) {
      throw new IllegalStateException("No mock to stub");
    }
  }
  
  @Override
  public Stubber<T> thenReturn(T object) {
    return null;
  }

  @Override
  public Stubber<T> thenThrow(Throwable throwable) {
    return null;
  }

  ASMMoxyEngine getEngine() {
    return engine;
  }

  Invocation getTheInvocation() {
    return theInvocation;
  }
}
