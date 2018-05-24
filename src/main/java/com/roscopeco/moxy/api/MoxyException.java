package com.roscopeco.moxy.api;

/**
 * Base-class for Moxy exceptions.
 * 
 * @author Ross.Bamford
 */
public class MoxyException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MoxyException() {
  }

  public MoxyException(String message) {
    super(message);
  }

  public MoxyException(Throwable cause) {
    super(cause);
  }

  public MoxyException(String message, Throwable cause) {
    super(message, cause);
  }
}
