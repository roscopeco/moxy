package com.roscopeco.moxy.impl.asm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.matchers.MoxyMatcher;

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
  
  private String buildArgsString(Invocation invocation) {
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
  
  private String readableTimes(int times) {
    if (times == 0) {
      return "zero times";
    } else if (times == 1) {
      return "once";
    } else if (times == 2) {
      return "twice";
    } else {
      return "" + times + " times";
    }
  }
  
  // suppress because we check manually
  @SuppressWarnings("unchecked")
  boolean argsMatch(List<Object> actualArgs, List<Object> storedArgs) {
    if (storedArgs.size() != actualArgs.size()) {
      return false;
    }
    
    boolean result = true;
    
    for (int i = 0; i < storedArgs.size(); i++) {
      Object stored = storedArgs.get(i);
      Object actual = actualArgs.get(i);
      
      if (stored instanceof MoxyMatcher) {
        MoxyMatcher<Object> matcher = (MoxyMatcher<Object>)stored;
        if (!matcher.matches(actual)) {
          result = false;
        }        
      } else {
        if (!stored.equals(actual)) {
          result = false;
        }
      }
    }
    
    return result;
  }
  
  @Override
  public MoxyVerifier wasCalled() {
    final Invocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    if (this.getEngine()
            .getRecorder()
            .getInvocationList(invocation.getReceiver().getClass(),
                               methodName, 
                               methodDesc)
        .stream()
        .anyMatch((e) -> argsMatch(e.getArgs(), invocation.getArgs())
    )) {
      return this;
    } else {
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " to be called " 
              + buildArgsString(invocation) 
              + "at least once but it wasn't called at all");
    }
  }
  
  int getCallCount(Invocation invocation, String methodName, String methodDesc) {
    return this.getEngine()
        .getRecorder()
        .getInvocationList(invocation.getReceiver().getClass(),
                           methodName, 
                           methodDesc)
    .stream()
    .filter( (e) -> argsMatch(e.getArgs(), invocation.getArgs()) )
    .collect(Collectors.toList())
    .size();    
  }

  @Override
  public MoxyVerifier wasCalled(int times) {
    final Invocation invocation = getTheInvocation();
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
              + "exactly " + readableTimes(times) + ", but it was called " 
              + readableTimes(actual));      
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
    final Invocation invocation = getTheInvocation();
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
              + "at least " + readableTimes(times) + ", but it was called " 
              + readableTimes(actual));
    }
  }

  @Override
  public MoxyVerifier wasCalledAtMost(int times) {
    final Invocation invocation = getTheInvocation();
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
              + "at most " + readableTimes(times) + ", but it was called " 
              + readableTimes(actual));
    }
  }
}
