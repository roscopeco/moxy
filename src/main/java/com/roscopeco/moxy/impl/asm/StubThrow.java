package com.roscopeco.moxy.impl.asm;

import java.util.List;

/*
 * This is used as the value in the stubbed throwMap
 * on the mocks.
 */
class StubThrow {
  List<Object> args;
  Throwable toThrow;
  
  public StubThrow(List<Object> args, Throwable toThrow) {
    super();
    this.args = args;
    this.toThrow = toThrow;
  } 
}