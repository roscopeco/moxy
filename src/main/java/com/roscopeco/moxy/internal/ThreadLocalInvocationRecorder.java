package com.roscopeco.moxy.internal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.roscopeco.moxy.api.Invocation;
import com.roscopeco.moxy.api.MoxyInvocationRecorder;

class ThreadLocalInvocationRecorder implements MoxyInvocationRecorder {
  private final ThreadLocal<LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>>
      invocationMapThreadLocal;
  
  private final ThreadLocal<Invocation> lastInvocationThreadLocal;
  
  ThreadLocalInvocationRecorder() {
    this.invocationMapThreadLocal = new ThreadLocal<>();
    this.lastInvocationThreadLocal = new ThreadLocal<>();
  }
  
  @Override
  public void recordInvocation(Object receiver, String methodName, String methodDesc, Object[] args) {
    final List<Invocation> invocations = ensureInvocationList(
        ensureInvocationMap(ensureLocalClassMap(), receiver.getClass()),
        methodName, methodDesc);
    
    Invocation invocation = new Invocation(receiver, methodName, methodDesc, args);
    
    // Add to list of invocations
    invocations.add(invocation);
    
    // Record last invocation on this thread
    this.lastInvocationThreadLocal.set(invocation);
  }
  
  @Override
  public Invocation getLastInvocation() {
    return this.lastInvocationThreadLocal.get();
  }

  private LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> ensureLocalClassMap() {
    LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>
        classMap = this.invocationMapThreadLocal.get();
    
    if (classMap == null) {
      this.invocationMapThreadLocal.set(classMap = new LinkedHashMap<>());
    }
    
    return classMap;    
  }
  
  private LinkedHashMap<String, List<Invocation>> ensureInvocationMap(
      LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>> classMap,
      Class<?> forClz) {
    LinkedHashMap<String, List<Invocation>> invocationMap = classMap.get(forClz);
    
    if (invocationMap == null) {
      classMap.put(forClz, invocationMap = new LinkedHashMap<>());
    }
    
    return invocationMap;
  }
  
  private List<Invocation> ensureInvocationList(
      LinkedHashMap<String, List<Invocation>> invocationMap,
      String forMethodName, String forMethodDescriptor) {
    List<Invocation> list = invocationMap.get(forMethodName + forMethodDescriptor);
    
    if (list == null) {
      invocationMap.put(forMethodName + forMethodDescriptor, list = new ArrayList<>());      
    }
    
    return list;
  }  
}
