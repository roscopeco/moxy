package com.roscopeco.moxy.api;

/**
 * Thrown to indicate the framework was expecting to see a mock
 * invocation (e.g. in a when(...) or assertMock(...) call) but
 * no such invocation was found.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class InvalidMockInvocationException extends MoxyException {
  private static final long serialVersionUID = 1L;

  public InvalidMockInvocationException() {
    super();
  }

  public InvalidMockInvocationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidMockInvocationException(String message) {
    super(message);
  }

  public InvalidMockInvocationException(Throwable cause) {
    super(cause);
  }
}
