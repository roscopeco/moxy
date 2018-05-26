package com.roscopeco.moxy.impl.asm;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.api.MoxyVerifier;

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
        .anyMatch((e) -> this.getEngine()
                             .getASMMatcherEngine()
                             .argsMatch(e.getArgs(), invocation.getArgs())
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
    .filter( (e) -> this.getEngine()
                        .getASMMatcherEngine()
                        .argsMatch(e.getArgs(), invocation.getArgs()) )
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
  
  int countExceptionsThrown(final Invocation invocation,
                            final String methodName, 
                            final String methodDesc,
                            final Predicate<? super Invocation> filterPredicate) {
    return this.getEngine()
        .getRecorder()
        .getInvocationList(invocation.getReceiver().getClass(),
                           methodName, 
                           methodDesc)
      .stream()
      .filter( (e) -> this.getEngine()
          .getASMMatcherEngine()
          .argsMatch(e.getArgs(), invocation.getArgs()) )
      .filter(filterPredicate)
      .collect(Collectors.toList())
      .size();
  }
  
  public MoxyVerifier neverThrew(Class<? extends Throwable> throwableClass) {
    Invocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    int actual = (countExceptionsThrown(invocation, 
                                        invocation.getMethodName(), 
                                        invocation.getMethodDesc(),
        (e) -> e.getThrew() != null && e.getThrew().getClass().equals(throwableClass))
    );
    
    if (actual > 0) {
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " "
              + buildArgsString(invocation) 
              + "never to throw exception class " + throwableClass.getName()
              + ", but it was thrown " + readableTimes(actual));
          
    } else {
      return this;
    }
  }
  
  public MoxyVerifier neverThrew(Throwable throwable) {
    final Invocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    int actual = (countExceptionsThrown(invocation, 
                                        invocation.getMethodName(), 
                                        invocation.getMethodDesc(),
        (e) -> e.getThrew() != null && e.getThrew().equals(throwable))
    );
    
    if (actual > 0) {
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " "
              + buildArgsString(invocation) 
              + "never to throw exception " + throwable
              + ", but it was thrown " + readableTimes(actual));
          
    } else {
      return this;
    }
  }
  
  public MoxyVerifier neverThrewAnyException() {
    Invocation invocation = getTheInvocation();
    final String methodName = invocation.getMethodName();
    final String methodDesc = invocation.getMethodDesc();
    
    int actual = (countExceptionsThrown(invocation, 
                                        invocation.getMethodName(), 
                                        invocation.getMethodDesc(),
        (e) -> e.getThrew() != null)
    );
    
    if (actual > 0) {
      throw new AssertionFailedError(
          "Expected mock " + methodName + ellipsisDesc(methodDesc) + " " 
              + buildArgsString(invocation) 
              + "never to throw any exception, but exceptions were thrown " + readableTimes(actual));
    } else {
      return this;
    }
  } 
}
