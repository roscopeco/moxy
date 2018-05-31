package com.roscopeco.moxy.api;

/**
 * Thrown to indicate an invocation has been incorrectly stubbed.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class InvalidStubbingException extends MoxyException {
  private static final long serialVersionUID = 1L;

  public InvalidStubbingException(String message) {
    super(message);
  }

  public InvalidStubbingException(String message, Throwable cause) {
    super(message, cause);
  }
}
