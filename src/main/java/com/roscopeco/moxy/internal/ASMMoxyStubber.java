package com.roscopeco.moxy.internal;

import java.lang.reflect.Field;

import com.roscopeco.moxy.api.MoxyStubber;

class ASMMoxyStubber<T> extends HasEngineAndInvocation implements MoxyStubber<T> {
  public ASMMoxyStubber(ASMMoxyEngine engine) {
    super(engine);
  }
  
  @Override
  public MoxyStubber<T> thenReturn(T object) {
    ASMMockSupport receiver = (ASMMockSupport)this.theInvocation.getReceiver();
    
    String methodReturnFieldName = TypesAndDescriptors.makeMethodReturnFieldName(
        this.theInvocation.getMethodName(), this.theInvocation.getMethodDesc());
    
    try {
      Field f = receiver.getClass().getDeclaredField(methodReturnFieldName);
      f.set(receiver, object);
    } catch (Exception e) {
      throw new IllegalStateException("Mock doesn't conform to expectations - are you using a different mock engine?\n"
                                    + "(This stubber is for the default ASMMoxyEngine).",
                                    e);
    }
        
    return this;
  }

  @Override
  public MoxyStubber<T> thenThrow(Throwable throwable) {
    return null;
  }
}
