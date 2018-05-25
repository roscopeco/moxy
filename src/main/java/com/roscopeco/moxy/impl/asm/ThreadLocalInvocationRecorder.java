package com.roscopeco.moxy.impl.asm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.matchers.InconsistentMatchersException;
import com.roscopeco.moxy.matchers.MoxyMatcher;

public class ThreadLocalInvocationRecorder {
  private final ASMMoxyEngine engine;
  
  private final ThreadLocal<LinkedHashMap<Class<?>, LinkedHashMap<String, List<Invocation>>>>
      invocationMapThreadLocal;
  
  private final ThreadLocal<Invocation> lastInvocationThreadLocal;
  
  ThreadLocalInvocationRecorder(ASMMoxyEngine engine) {
    this.engine = engine;
    this.invocationMapThreadLocal = new ThreadLocal<>();
    this.lastInvocationThreadLocal = new ThreadLocal<>();
  }
  
  /*
   * This gets any matchers from the MatcherEngine's stack, and replaces
   * the arguments from the last invocation with them.
   */
  void replaceInvocationArgsWithMatchers() {
    final List<MoxyMatcher<?>> matchers = this.engine.getASMMatcherEngine().popMatchers();
    if (matchers != null) {
      final Invocation lastInvocation = this.getLastInvocation();
      
      final List<Object> lastArgs = lastInvocation.getArgs();
      if (lastArgs.size() != matchers.size()) {
        throw new InconsistentMatchersException();
      } else {
        for (int i = 0; i < lastArgs.size(); i++) {
          lastArgs.set(i, matchers.get(i));          
        }
      }      
    }
  }
  
  // public as it's accessed from mocks (in different packages).
  public void recordInvocation(Object receiver, String methodName, String methodDesc, List<Object> args) {
    final List<Invocation> invocations = ensureInvocationList(
        ensureInvocationMap(ensureLocalClassMap(), receiver.getClass()),
        methodName, methodDesc);
    
    Invocation invocation = new Invocation(receiver, methodName, methodDesc, args);
    
    // Add to list of invocations
    invocations.add(invocation);
    
    // Record last invocation on this thread
    this.lastInvocationThreadLocal.set(invocation);
    
    // Fixup matchers
    replaceInvocationArgsWithMatchers();
  }
  
  /*
   * NOTE This does exactly what it says - deletes the invocation from the
   * list, but not from the last invocation thread local. This means the engine
   * can still use the last invocation for the args.
   * 
   * @see comments on {@link MoxyInvocationRecorder#unrecordLastInvocation}.
   */
  void unrecordLastInvocation() {
    final Invocation lastInvocation = this.getLastInvocation();
    
    if (lastInvocation != null) {
      final List<Invocation> invocations = ensureInvocationList(
          ensureInvocationMap(ensureLocalClassMap(), 
                              lastInvocation.getReceiver().getClass()),
                              lastInvocation.getMethodName(), 
                              lastInvocation.getMethodDesc());
      
          invocations.remove(invocations.size() - 1);
    }
  }
  
  List<Invocation> getInvocationList(Class<?> forClz, String methodName, String methodDesc) {
    return ensureInvocationList(ensureInvocationMap(ensureLocalClassMap(), forClz), methodName, methodDesc);
  }

  Invocation getLastInvocation() {
    return this.lastInvocationThreadLocal.get();
  }

  Invocation getAndClearLastInvocation() {
    Invocation invocation = getLastInvocation();
    this.lastInvocationThreadLocal.set(null);
    return invocation;
  }

  void reset() {
    this.lastInvocationThreadLocal.set(null);
    this.invocationMapThreadLocal.set(null);    
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
