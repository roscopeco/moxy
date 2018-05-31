package com.roscopeco.moxy.impl.asm;

import java.util.List;

/*
 * This is used as the value in the stubbed superMap
 * on the mocks.
 */
class StubSuper {
  final List<Object> args;
  final boolean callSuper;   

  public StubSuper(final List<Object> args, final boolean callSuper) {
    super();
    this.args = args;
    this.callSuper = callSuper;
  }  
}