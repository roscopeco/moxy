package com.roscopeco.moxy.internal;

import java.lang.reflect.Field;

import com.roscopeco.moxy.api.Invocation;
import com.roscopeco.moxy.api.Stubber;

class ASMStubber<T> implements Stubber<T> {
  private final ASMMoxyEngine engine;
  private final Invocation theInvocation;
  
  public ASMStubber(ASMMoxyEngine engine) {
    this.engine = engine;
    this.theInvocation = engine.getRecorder().getLastInvocation();

    if (this.theInvocation == null || 
        this.theInvocation.getReceiver() == null || 
        !engine.isMock(this.theInvocation.getReceiver().getClass())) {
      throw new IllegalStateException("No mock to stub");
    }
  }
  
  @Override
  public Stubber<T> thenReturn(T object) {
    ASMMoxyMockSupport receiver = (ASMMoxyMockSupport)this.theInvocation.getReceiver();
    
    String methodReturnFieldName = TypesAndDescriptors.makeMethodReturnFieldName(
        this.theInvocation.getMethodName(), theInvocation.getMethodDesc());
    
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
