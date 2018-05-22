package com.roscopeco.moxy.api;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
  
  @Override
  public int hashCode() {
    return Objects.hash(this.receiver, this.methodName, this.methodDesc, this.args);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MoxyInvocation other = (MoxyInvocation) obj;
    if (args == null) {
      if (other.args != null)
        return false;
    } else if (!args.equals(other.args))
      return false;
    if (methodDesc == null) {
      if (other.methodDesc != null)
        return false;
    } else if (!methodDesc.equals(other.methodDesc))
      return false;
    if (methodName == null) {
      if (other.methodName != null)
        return false;
    } else if (!methodName.equals(other.methodName))
      return false;
    if (receiver == null) {
      if (other.receiver != null)
        return false;
    } else if (!receiver.equals(other.receiver))
      return false;
    return true;
  }
}
