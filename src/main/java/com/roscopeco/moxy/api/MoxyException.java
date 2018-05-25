package com.roscopeco.moxy.api;

/**
 * Base-class for Moxy exceptions.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
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
