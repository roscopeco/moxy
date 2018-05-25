package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

public class PossibleMatcherUsageError extends MoxyException {
  private static final long serialVersionUID = 1L;
  
  public PossibleMatcherUsageError(String message, Throwable cause) {
    super(message, cause);
  }
}
