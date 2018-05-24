package com.roscopeco.moxy.impl.asm;

import java.util.HashMap;

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
  public ASMMoxyEngine __moxy_asm_getEngine();
  public HashMap<Invocation, Object> __moxy_asm_getReturnMap();
  public HashMap<Invocation, Throwable> __moxy_asm_getThrowMap();
  
  /* Implemented by default */
  default public ThreadLocalInvocationRecorder __moxy_asm_getRecorder() {
    return __moxy_asm_getEngine().getRecorder();    
  }
  
  default public void __moxy_asm_throwNullConstructorException() throws InstantiationException {
    throw new InstantiationException("Cannot construct mock with null constructor");
  }
  
  default public void __moxy_asm_setThrowOrReturn(Invocation invocation,
                                                  Object object,
                                                  boolean isReturn) {    

    final HashMap<Invocation, Object> returnMap = __moxy_asm_getReturnMap();
    final HashMap<Invocation, Throwable> throwMap = __moxy_asm_getThrowMap();
    
    // This'll work right up until we start setting these elsewhere...
    synchronized(this) {
      if (isReturn) {
        if (throwMap.containsKey(invocation)) {
          // throw is set - can't do both!
          throw new IllegalStateException("Cannot set both throw and return for " 
                                          + invocation.getMethodName()
                                          + invocation.getMethodDesc());  
        } else {
          returnMap.put(invocation, object);
        }
      } else {
        if (returnMap.containsKey(invocation)) {
          // return is set - can't do both!
          throw new IllegalStateException("Cannot set both throw and return for " 
                                          + invocation.getMethodName()
                                          + invocation.getMethodDesc());  
        } else {
          if (!(object instanceof Throwable)) {
            // should never happen - code calling this shouldn't allow it, but hey ho...
            throw new IllegalArgumentException("Cannot throw instance of " + object.getClass());
          } else {
            throwMap.put(invocation, (Throwable)object);
          }
        }
      }
    }
  }
  
  // NOTE only to be used for void methods!
  default public void __moxy_asm_setThrowForVoidMethods(Invocation invocation,
                                                             Object object) {
    final HashMap<Invocation, Throwable> throwMap = __moxy_asm_getThrowMap();

    if (!(object instanceof Throwable)) {
      // should never happen - code calling this shouldn't allow it, but hey ho...
      throw new IllegalArgumentException("Cannot throw instance of " + object.getClass());
    } else {
      throwMap.put(invocation, (Throwable)object);
    }
  }
  
  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current 
   * invocation just prior to throw or return.
   */
  default public Throwable __moxy_asm_getThrowForCurrentInvocation() {
    final Invocation current = __moxy_asm_getEngine().getRecorder().getLastInvocation();
    final HashMap<Invocation, Throwable> throwMap = __moxy_asm_getThrowMap();    
    return throwMap.get(current);
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current 
   * invocation just prior to throw or return.
   */
  default public Object __moxy_asm_getReturnForCurrentInvocation() {
    final Invocation current = __moxy_asm_getEngine().getRecorder().getLastInvocation();
    final HashMap<Invocation, Object> returnMap = __moxy_asm_getReturnMap();    
    return returnMap.get(current);
  }
}
