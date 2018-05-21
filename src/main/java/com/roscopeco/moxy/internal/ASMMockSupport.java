package com.roscopeco.moxy.internal;

import java.lang.reflect.Field;

import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyInvocationRecorder;

/**
 * All mocks implement this interface. It (ab)uses default methods
 * to allow us to do less bytecode generation.
 * 
 * Don't implement this yourself, it's only public because it needs
 * to be visible to subclasses.
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
  
  default public void __moxy_asm_setThrowOrReturnField(String methodName,
                                                       String methodDesc,
                                                       Object object,
                                                       boolean isReturn) {
    
    final String returnFieldName = TypesAndDescriptors.makeMethodReturnFieldName(
        methodName, methodDesc);
    
    final String throwFieldName = TypesAndDescriptors.makeMethodThrowFieldName(
        methodName, methodDesc);
    
    try {
      final Field checkField, setField;
      
      if (isReturn) {
        checkField = this.getClass().getDeclaredField(throwFieldName);
        setField = this.getClass().getDeclaredField(returnFieldName);
      } else {
        checkField = this.getClass().getDeclaredField(returnFieldName);
        setField = this.getClass().getDeclaredField(throwFieldName);
      }

      // This'll work right up until we start setting these fields elsewhere...
      synchronized(this) {
        if (checkField.get(this) != null) {
          // other field is set - can't do both!
          throw new IllegalStateException("Cannot set both throw and return for " + methodName + methodDesc);
        }

        setField.set(this, object);
      }
      
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Mock doesn't conform to expectations - are you using a different mock engine?\n"
                                    + "(This stubber is for the default ASMMoxyEngine).",
                                      e);
    }    
  }
  
  // NOTE only to be used for void methods!
  default public void __moxy_asm_setThrowFieldForVoidMethods(String methodName,
                                                        String methodDesc,
                                                        Object object) {
    final String throwFieldName = TypesAndDescriptors.makeMethodThrowFieldName(
        methodName, methodDesc);
    
    try {
      final Field throwField = this.getClass().getDeclaredField(throwFieldName);
      throwField.set(this, object);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Mock doesn't conform to expectations - are you using a different mock engine?\n"
          + "(This stubber is for the default ASMMoxyEngine).",
            e);
    }    
  }
}
