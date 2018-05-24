package com.roscopeco.moxy.impl.asm;

import com.roscopeco.moxy.api.MoxyStubber;

class ASMMoxyStubber<T> extends HasEngineAndInvocation implements MoxyStubber<T> {
  public ASMMoxyStubber(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyStubber<T> thenReturn(T object) {
    final Invocation invocation = this.theInvocation;
    ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();
    
    receiver.__moxy_asm_setThrowOrReturn(invocation, object, true);
        
    return this;
  }

  @Override
  public MoxyStubber<T> thenThrow(Throwable throwable) {
    final Invocation invocation = this.theInvocation;
    ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();
    
    receiver.__moxy_asm_setThrowOrReturn(invocation, throwable, false);
        
    return this;
  }
}
