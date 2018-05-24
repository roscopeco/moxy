package com.roscopeco.moxy.model;

public class MethodWithArgAndReturn {
  public String sayHelloTo(String who) {
    return "Hello, " + who;
  }
  
  public String hasTwoArgs(String arg1, int arg2) {
    return "" + arg1 + arg2;
  }
}
