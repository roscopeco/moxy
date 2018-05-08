package com.roscopeco.moxy.internal;

import com.roscopeco.moxy.api.MoxyInvocation;

class HasEngineAndInvocation {
  protected final ASMMoxyEngine engine;
  protected final MoxyInvocation theInvocation;

  public HasEngineAndInvocation(ASMMoxyEngine engine) {
    this.engine = engine;
    this.theInvocation = engine.getRecorder().getLastInvocation();

    if (this.theInvocation == null || 
        this.theInvocation.getReceiver() == null || 
        !engine.isMock(this.theInvocation.getReceiver().getClass())) {
      throw new IllegalStateException("No mock invocation found");
    }
  }

  protected ASMMoxyEngine getEngine() {
    return engine;
  }

  protected MoxyInvocation getTheInvocation() {
    return theInvocation;
  }
}