package com.roscopeco.moxy.api;

public class MonitoredInvocationException extends MoxyException {
  private static final long serialVersionUID = 1L;

  public MonitoredInvocationException(final Throwable cause) {
    super("An unexpected exception occurred during a monitored invocation; See cause", cause);
  }
}
