package com.roscopeco.moxy.internal;

import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyInvocationRecorder;

/**
 * All mocks implement this interface. It (ab)uses default methods
 * to allow us to do less bytecode generation.
 * 
 * @author Ross.Bamford
 *
 */
public interface ASMMockSupport {
  /* For mocks to implement */
  public MoxyEngine __moxy_asm_getEngine();
  
  /* Implemented by default */
  default public MoxyInvocationRecorder __moxy_asm_getRecorder() {
    return __moxy_asm_getEngine().getRecorder();    
  }
  
  default public void __moxy_asm_throwNullConstructorException() throws InstantiationException {
    throw new InstantiationException("Cannot construct mock with null constructor");
  }
}
