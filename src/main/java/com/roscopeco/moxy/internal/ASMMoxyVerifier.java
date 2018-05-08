package com.roscopeco.moxy.internal;

import com.roscopeco.moxy.api.MoxyVerifier;

class ASMMoxyVerifier<T> extends HasEngineAndInvocation implements MoxyVerifier<T> {
  public ASMMoxyVerifier(ASMMoxyEngine engine) {
    super(engine);
  }
}
