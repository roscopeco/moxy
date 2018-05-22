package com.roscopeco.moxy.impl.asm;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.MoxyInvocation;
import com.roscopeco.moxy.api.MoxyVerifier;

import junit.framework.AssertionFailedError;

class ASMMoxyVerifier extends HasEngineAndInvocation implements MoxyVerifier {
  public ASMMoxyVerifier(ASMMoxyEngine engine) {
    super(engine);
  }
  
  private String inspectArg(Object arg) {
    if (arg instanceof String) {
      return "\"" + arg + "\"";
    } else if (arg instanceof Character) {
      return "'" + arg + "'";
    } else {
      return arg.toString();
    }
  }
  
  private String buildArgsString(MoxyInvocation invocation) {
    String args = invocation.getArgs().stream()
        .map((e) -> inspectArg(e))
        .collect(Collectors.joining(", "));
    
    if (args.isEmpty()) {
      return args;
    } else {
      return "with arguments (" + args + ") ";      
    }
  }
  
  private String ellipsisDesc(String descriptor) {    
    if (descriptor.contains("()")) {
      return "()";
    } else {
      return "(" + Arrays.stream(Type.getArgumentTypes(descriptor))
          .map((e) -> e.getClassName())
          .collect(Collectors.joining(", ")) + ")";
    }
  }
  
  @Override
  public MoxyVerifier wasCalled() {
    final MoxyInvocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    if (this.getEngine()
            .getRecorder()
            .getInvocationList(invocation.getReceiver().getClass(),
                               methodName, 
                               methodDesc)
        .stream()
        .anyMatch((e) -> e.getArgs().equals(invocation.getArgs()) 
    )) {
      return this;
    } else {
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " to be called " 
              + buildArgsString(invocation) 
              + "at least once but it wasn't");
    }
  }
  
  int getCallCount(MoxyInvocation invocation, String methodName, String methodDesc) {
    return this.getEngine()
        .getRecorder()
        .getInvocationList(invocation.getReceiver().getClass(),
                           methodName, 
                           methodDesc)
    .stream()
    .filter( (e) -> e.getArgs().equals(invocation.getArgs()))
    .collect(Collectors.toList())
    .size();    
  }

  @Override
  public MoxyVerifier wasCalled(int times) {
    final MoxyInvocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    final int actual = getCallCount(invocation, methodName, methodDesc);
    
    if (actual == times) {
      return this;
    } else {
      // TODO Stringbuilder...... on both of these (above).
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " to be called " 
              + buildArgsString(invocation) 
              + "exactly " + times + " time(s), but it was called " 
              + actual + " time(s)");
    }
  }

  @Override
  public MoxyVerifier wasNotCalled() {
    return wasCalled(0);
  }

  @Override
  public MoxyVerifier wasCalledOnce() {
    return wasCalled(1);
  }

  @Override
  public MoxyVerifier wasCalledTwice() {
    return wasCalled(2);
  }

  @Override
  public MoxyVerifier wasCalledAtLeast(int times) {
    final MoxyInvocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    final int actual = getCallCount(invocation, methodName, methodDesc);
    
    if (actual >= times) {
      return this;
    } else {
      // TODO Stringbuilder...... on both of these (above).
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " to be called " 
              + buildArgsString(invocation) 
              + "at least " + times + " time(s), but it was called " 
              + actual + " time(s)");
    }
  }

  @Override
  public MoxyVerifier wasCalledAtMost(int times) {
    final MoxyInvocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    final int actual = getCallCount(invocation, methodName, methodDesc);
    
    if (actual <= times) {
      return this;
    } else {
      // TODO Stringbuilder...... on both of these (above).
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " to be called " 
              + buildArgsString(invocation) 
              + "at most " + times + " time(s), but it was called " 
              + actual + " time(s)");
    }
  }
}
