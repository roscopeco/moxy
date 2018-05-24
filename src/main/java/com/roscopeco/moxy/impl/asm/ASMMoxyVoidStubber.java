package com.roscopeco.moxy.impl.asm;

import com.roscopeco.moxy.api.MoxyVoidStubber;

class ASMMoxyVoidStubber extends HasEngineAndInvocation implements MoxyVoidStubber {
  public ASMMoxyVoidStubber(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyVoidStubber thenThrow(Throwable throwable) {
    final Invocation invocation = this.theInvocation;
    ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();
    
    receiver.__moxy_asm_setThrowOrReturn(invocation, throwable, false);
        
    return this;
  }
}
