package com.roscopeco.moxy.api;

/**
 * Represents a single invocation of a given method, on a given receiver,
 * with given arguments.
 * 
 * @author Ross.Bamford
 *
 */
public final class Invocation {
  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  
  private final Object receiver;
  private final String methodNameAndSig;
  private final Object[] args;

  /**
   * Create a new Invocation with the specified receiver, method and arguments.
   * 
   * @param receiver
   * @param methodNameAndSig
   * @param args
   */
  public Invocation(final Object receiver, final String methodNameAndSig, final Object[] args) {
    if (receiver == null || methodNameAndSig == null || methodNameAndSig.isEmpty()) {
      throw new IllegalArgumentException("Cannot create invocation: receiver and/or methodNameAndSig are null (or empty)");
    }
    
    this.receiver = receiver;
    this.methodNameAndSig = methodNameAndSig;
    this.args = args;      
  }

  /**
   * @return the receiver.
   */
  public Object getReceiver() {
    return receiver;
  }

  /**
   * @return the invoked method name and signature.
   */
  public String getMethodNameAndSig() {
    return methodNameAndSig;
  }

  /**
   * @return the arguments the method was called with. Possibly empty, never null.
   */
  public Object[] getArgs() {
    return args == null ? EMPTY_OBJECT_ARRAY : args;
  }
}
