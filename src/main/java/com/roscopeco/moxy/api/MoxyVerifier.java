package com.roscopeco.moxy.api;

public interface MoxyVerifier<T> {
  public MoxyVerifier<T> wasCalled();
  public MoxyVerifier<T> wasCalled(int times);
}
