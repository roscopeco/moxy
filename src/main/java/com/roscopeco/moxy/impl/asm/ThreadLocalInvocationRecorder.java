package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.roscopeco.moxy.api.MoxyInvocation;
import com.roscopeco.moxy.api.MoxyInvocationRecorder;

class ThreadLocalInvocationRecorder implements MoxyInvocationRecorder {
  private final ThreadLocal<LinkedHashMap<Class<?>, LinkedHashMap<String, List<MoxyInvocation>>>>
      invocationMapThreadLocal;
  
  private final ThreadLocal<MoxyInvocation> lastInvocationThreadLocal;
  
  ThreadLocalInvocationRecorder() {
    this.invocationMapThreadLocal = new ThreadLocal<>();
    this.lastInvocationThreadLocal = new ThreadLocal<>();
  }
  
  @Override
  public void recordInvocation(Object receiver, String methodName, String methodDesc, Object[] args) {
    final List<MoxyInvocation> invocations = ensureInvocationList(
        ensureInvocationMap(ensureLocalClassMap(), receiver.getClass()),
        methodName, methodDesc);
    
    MoxyInvocation invocation = new MoxyInvocation(receiver, methodName, methodDesc, args);
    
    // Add to list of invocations
    invocations.add(invocation);
    
    // Record last invocation on this thread
    this.lastInvocationThreadLocal.set(invocation);
  }
  
  /*
   * NOTE This does exactly what it says - deletes the invocation from the
   * list, but not from the last invocation thread local. This means the engine
   * can still use the last invocation for the args.
   * 
   * @see comments on {@link MoxyInvocationRecorder#unrecordLastInvocation}.
   */
  public void unrecordLastInvocation() {
    final MoxyInvocation lastInvocation = this.getLastInvocation();
    
    if (lastInvocation != null) {
      final List<MoxyInvocation> invocations = ensureInvocationList(
          ensureInvocationMap(ensureLocalClassMap(), 
                              lastInvocation.getReceiver().getClass()),
                              lastInvocation.getMethodName(), 
                              lastInvocation.getMethodDesc());
      
          invocations.remove(invocations.size() - 1);
    }
  }
  
  @Override
  public List<MoxyInvocation> getInvocationList(Class<?> forClz, String methodName, String methodDesc) {
    return ensureInvocationList(ensureInvocationMap(ensureLocalClassMap(), forClz), methodName, methodDesc);
  }

  @Override
  public MoxyInvocation getLastInvocation() {
    return this.lastInvocationThreadLocal.get();
  }

  @Override
  public void reset() {
    this.lastInvocationThreadLocal.set(null);
    this.invocationMapThreadLocal.set(null);    
  }

  private LinkedHashMap<Class<?>, LinkedHashMap<String, List<MoxyInvocation>>> ensureLocalClassMap() {
    LinkedHashMap<Class<?>, LinkedHashMap<String, List<MoxyInvocation>>>
        classMap = this.invocationMapThreadLocal.get();
    
    if (classMap == null) {
      this.invocationMapThreadLocal.set(classMap = new LinkedHashMap<>());
    }
    
    return classMap;    
  }
  
  private LinkedHashMap<String, List<MoxyInvocation>> ensureInvocationMap(
      LinkedHashMap<Class<?>, LinkedHashMap<String, List<MoxyInvocation>>> classMap,
      Class<?> forClz) {
    LinkedHashMap<String, List<MoxyInvocation>> invocationMap = classMap.get(forClz);
    
    if (invocationMap == null) {
      classMap.put(forClz, invocationMap = new LinkedHashMap<>());
    }
    
    return invocationMap;
  }
  
  private List<MoxyInvocation> ensureInvocationList(
      LinkedHashMap<String, List<MoxyInvocation>> invocationMap,
      String forMethodName, String forMethodDescriptor) {
    List<MoxyInvocation> list = invocationMap.get(forMethodName + forMethodDescriptor);
    
    if (list == null) {
      invocationMap.put(forMethodName + forMethodDescriptor, list = new ArrayList<>());      
    }
    
    return list;
  }  
}
