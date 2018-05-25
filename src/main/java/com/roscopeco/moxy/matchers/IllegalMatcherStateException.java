package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

public class IllegalMatcherStateException extends MoxyException {
  private static final long serialVersionUID = 1L;
  
  public IllegalMatcherStateException(String message) {
    super(message);
  }
}
