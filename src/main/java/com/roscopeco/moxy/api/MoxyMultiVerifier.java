package com.roscopeco.moxy.api;

public interface MoxyMultiVerifier {
  public MoxyMultiVerifier inOrder();
  public MoxyMultiVerifier exclusivelyInOrder();

  public void wereNotCalled();
  public void wereAllCalled();
}
