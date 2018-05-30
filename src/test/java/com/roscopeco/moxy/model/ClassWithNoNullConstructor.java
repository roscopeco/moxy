package com.roscopeco.moxy.model;

public class ClassWithNoNullConstructor {
  final String anything;
  final int anyInt;
  
  public ClassWithNoNullConstructor(String anything) { 
    this.anything = anything;
    this.anyInt = 42;
  }
  
  public ClassWithNoNullConstructor(int anyInt) {
    this.anything = "nothing";
    this.anyInt = anyInt;
  }
  
  public ClassWithNoNullConstructor(float f, double d, long l) {
    this.anything = "nothing";
    this.anyInt = 42;
  }
  
  public String returnSomething() {
    return anything;
  }
  
  public int getAnyInt() {
    return anyInt;
  }
}
