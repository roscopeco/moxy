package com.roscopeco.moxy.api;

/**
 * Thrown to indicate an error occured during mock generation.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class MockGenerationException extends MoxyException {
  private static final long serialVersionUID = 1L;

  public MockGenerationException() {
  }

  public MockGenerationException(String message) {
    super(message);
  }

  public MockGenerationException(Throwable cause) {
    super(cause);
  }

  public MockGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
