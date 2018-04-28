package com.roscopeco.moxy.api;

public interface Stubber<T> {
  public Stubber<T> thenReturn(T object);
  public Stubber<T> thenThrow(Throwable throwable);
}
