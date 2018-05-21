package com.roscopeco.moxy.api;

import java.util.Collections;
import java.util.List;

/**
 * Represents a single invocation of a given method, on a given receiver,
 * with given arguments.
 * 
 * @author Ross.Bamford
 *
 */
public final class MoxyInvocation {
  private static final List<Object> EMPTY_OBJECT_LIST = Collections.emptyList();
  
  private final Object receiver;
  private final String methodName;
  private final String methodDesc;
  private final List<Object> args;

  /**
   * Create a new Invocation with the specified receiver, method and arguments.
   * 
   * @param receiver
   * @param methodNameAndSig
   * @param args
   */
  public MoxyInvocation(final Object receiver, final String methodName, final String methodDesc, final List<Object> args) {
    if (receiver == null || 
        methodName == null || 
        methodName.isEmpty() || 
        methodDesc == null || 
        methodDesc.isEmpty()) {
      throw new IllegalArgumentException("Cannot create invocation: receiver and/or methodName/methodSig are null (or empty)");
    }
    
    this.receiver = receiver;
    this.methodName = methodName;
    this.methodDesc = methodDesc;
    this.args = args;      
  }

  /**
   * @return the receiver.
   */
  public Object getReceiver() {
    return receiver;
  }

  /**
   * @return the invoked method name.
   */
  public String getMethodName() {
    return methodName;
  }
  
  /**
   * @return the invoked method's descriptor;
   */
  public String getMethodDesc() {
    return methodDesc;
  }

  /**
   * @return the arguments the method was called with. Possibly empty, never null.
   */
  public List<Object> getArgs() {
    return args == null ? EMPTY_OBJECT_LIST : args;
  }
}
