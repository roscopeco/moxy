package com.roscopeco.moxy.api;

public interface MoxyStubber<T> {
  public MoxyStubber<T> thenReturn(T object);
  public MoxyStubber<T> thenThrow(Throwable throwable);
}
