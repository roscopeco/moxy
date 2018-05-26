package com.roscopeco.moxy.impl.asm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 * All mocks implement this interface. It (ab)uses default methods
 * to allow us to do less bytecode generation.
 * 
 * Don't implement this yourself, it's only public because it needs
 * to be visible to subclasses.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 *
 */
public interface ASMMockSupport {
  /* For mocks to implement */
  public ASMMoxyEngine __moxy_asm_getEngine();
  
  // Using Deque here for efficient add-at-front, so when
  // we stream we see the most recent stubbing...
  public HashMap<StubMethod, Deque<StubReturn>> __moxy_asm_getReturnMap();
  public HashMap<StubMethod, Deque<StubThrow>> __moxy_asm_getThrowMap();
  
  /* Implemented by default */
  default public ThreadLocalInvocationRecorder __moxy_asm_getRecorder() {
    return __moxy_asm_getEngine().getRecorder();    
  }
  
  default public void __moxy_asm_throwNullConstructorException() throws InstantiationException {
    throw new InstantiationException("Cannot construct mock with null constructor");
  }
  
  default public Deque<StubReturn>__moxy_asm_ensureStubReturnDeque(      
      HashMap<StubMethod, Deque<StubReturn>> returnMap,
      StubMethod method) {
    Deque<StubReturn> deque = returnMap.get(method);
    
    if (deque == null) {
      returnMap.put(method, deque = new ArrayDeque<>());      
    }
    
    return deque;
  }
  
  default public Deque<StubThrow>__moxy_asm_ensureStubThrowDeque(      
      HashMap<StubMethod, Deque<StubThrow>> throwMap,
      StubMethod method) {
    Deque<StubThrow> deque = throwMap.get(method);
    
    if (deque == null) {
      throwMap.put(method, deque = new ArrayDeque<>());      
    }
    
    return deque;    
  }
  
  default public void __moxy_asm_setThrowOrReturn(Invocation invocation,
                                                  Object object,
                                                  boolean isReturn) {    

    
    if (isReturn) {
      if (__moxy_asm_getThrowForInvocation(invocation) != null) {
        throw new IllegalStateException("Cannot set both throw and return for " 
                                      + invocation.getMethodName()
                                      + invocation.getMethodDesc());
      } else {
        final HashMap<StubMethod, Deque<StubReturn>> returnMap = __moxy_asm_getReturnMap();
        final Deque<StubReturn> deque = __moxy_asm_ensureStubReturnDeque(
            returnMap, 
            new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));
        
        deque.addFirst(new StubReturn(invocation.getArgs(), object));
      }
    } else {
      if (__moxy_asm_getReturnForInvocation(invocation) != null) {
        throw new IllegalStateException("Cannot set both throw and return for " 
                                      + invocation.getMethodName()
                                      + invocation.getMethodDesc());
      } else {
        final HashMap<StubMethod, Deque<StubThrow>> throwMap = __moxy_asm_getThrowMap();      
        final Deque<StubThrow> deque = __moxy_asm_ensureStubThrowDeque(
            throwMap, 
            new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));
        
        if (object instanceof Throwable) {      
          deque.addFirst(new StubThrow(invocation.getArgs(), (Throwable)object));
        } else {
          throw new IllegalArgumentException("Cannot throw non-Throwable class " + object.getClass().getName());
        }
      }
    }    
  }
  
  default public Throwable __moxy_asm_getThrowForInvocation(Invocation invocation) {
    final HashMap<StubMethod, Deque<StubThrow>> throwMap = __moxy_asm_getThrowMap();
    final ASMMoxyMatcherEngine matchEngine = this.__moxy_asm_getEngine().getASMMatcherEngine();
    final Deque<StubThrow> deque = throwMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));
    
    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (StubThrow stubThrow : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubThrow.args)) {
          return stubThrow.toThrow;
        }
      }
    }
    
    return null;
  }

  default public Object __moxy_asm_getReturnForInvocation(Invocation invocation) {
    final HashMap<StubMethod, Deque<StubReturn>> returnMap = __moxy_asm_getReturnMap();
    final ASMMoxyMatcherEngine matchEngine = this.__moxy_asm_getEngine().getASMMatcherEngine();
    final Deque<StubReturn> deque = returnMap.get(
        new StubMethod(invocation.getMethodName(), invocation.getMethodDesc()));
    
    // Old-fashioned but a tad more efficient...
    if (deque != null) {
      for (StubReturn stubReturn : deque) {
        if (matchEngine.argsMatch(invocation.getArgs(), stubReturn.args)) {
          return stubReturn.toReturn;
        }
      }
    }
    
    return null;
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current 
   * invocation just prior to throw or return.
   */
  default public Throwable __moxy_asm_getThrowForCurrentInvocation() {
    return __moxy_asm_getThrowForInvocation(
        __moxy_asm_getEngine().getRecorder().getLastInvocation());    
  }

  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current 
   * invocation just prior to throw or return.
   */
  default public Object __moxy_asm_getReturnForCurrentInvocation() {
    return __moxy_asm_getReturnForInvocation(
        __moxy_asm_getEngine().getRecorder().getLastInvocation());
  }
  
  /* This MUST only ever be called from mocked methods AFTER the invocation has been
   * recorded. It relies on the fact that getLastInvocation will always be the current 
   * invocation just prior to throw or return.
   */
  default public void __moxy_asm_updateCurrentInvocationReturnThrow(Object returned, Throwable threw) {
    final Invocation invocation = __moxy_asm_getEngine().getRecorder().getLastInvocation();
    invocation.setReturned(returned);
    invocation.setThrew(threw);
  }  
}
