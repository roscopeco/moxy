package com.roscopeco.moxy.internal;

import com.roscopeco.moxy.api.MoxyInvocation;
import com.roscopeco.moxy.api.MoxyStubber;

class ASMMoxyStubber<T> extends HasEngineAndInvocation implements MoxyStubber<T> {
  public ASMMoxyStubber(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyStubber<T> thenReturn(T object) {
    final MoxyInvocation invocation = this.theInvocation;
    ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();
    
    receiver.__moxy_asm_setThrowOrReturnField(invocation.getMethodName(),
                                              invocation.getMethodDesc(),
                                              object,
                                              true);
        
    return this;
  }

  @Override
  public MoxyStubber<T> thenThrow(Throwable throwable) {
    final MoxyInvocation invocation = this.theInvocation;
    ASMMockSupport receiver = (ASMMockSupport)invocation.getReceiver();
    
    receiver.__moxy_asm_setThrowOrReturnField(invocation.getMethodName(),
                                              invocation.getMethodDesc(),
                                              throwable,
                                              false);
        
    return this;
  }
}
