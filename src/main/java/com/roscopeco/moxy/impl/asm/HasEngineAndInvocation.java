package com.roscopeco.moxy.impl.asm;

class HasEngineAndInvocation {
  protected final ASMMoxyEngine engine;
  protected final Invocation theInvocation;

  public HasEngineAndInvocation(ASMMoxyEngine engine) {
    this.engine = engine;
    this.theInvocation = engine.getRecorder().getAndClearLastInvocation();

    if (this.theInvocation == null || 
        this.theInvocation.getReceiver() == null || 
        !engine.isMock(this.theInvocation.getReceiver().getClass())) {
      throw new IllegalStateException("No mock invocation found");
    }
  }

  protected ASMMoxyEngine getEngine() {
    return engine;
  }

  protected Invocation getTheInvocation() {
    return theInvocation;
  }
}