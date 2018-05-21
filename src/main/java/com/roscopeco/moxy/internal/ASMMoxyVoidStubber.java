package com.roscopeco.moxy.internal;

import com.roscopeco.moxy.api.MoxyInvocation;
import com.roscopeco.moxy.api.MoxyVoidStubber;

class ASMMoxyVoidStubber extends HasEngineAndInvocation implements MoxyVoidStubber {
  public ASMMoxyVoidStubber(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyVoidStubber thenThrow(Throwable throwable) {
    final MoxyInvocation invocation = this.theInvocation;
    ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();
    
    receiver.__moxy_asm_setThrowFieldForVoidMethods(invocation.getMethodName(),
                                                    invocation.getMethodDesc(),
                                                    throwable);
        
    return this;
  }
}
