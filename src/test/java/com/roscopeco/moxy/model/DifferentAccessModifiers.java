package com.roscopeco.moxy.model;

public class DifferentAccessModifiers {
  public void publicMethod() { }
  
  void defaultMethod() { }
  
  protected void protectedMethod() { }
  
  @SuppressWarnings("unused")
  private void privateMethod() { }
  
  final public void finalMethod() { }

}
