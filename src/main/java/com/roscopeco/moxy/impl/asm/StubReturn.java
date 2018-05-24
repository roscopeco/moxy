package com.roscopeco.moxy.impl.asm;

import java.util.List;

/*
 * This is used as the value in the stubbed returnMap
 * on the mocks.
 */
class StubReturn {
  final List<Object> args;
  final Object toReturn;    

  public StubReturn(final List<Object> args, final Object toReturn) {
    super();
    this.args = args;
    this.toReturn = toReturn;
  }  
}