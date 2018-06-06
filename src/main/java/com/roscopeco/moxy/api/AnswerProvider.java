package com.roscopeco.moxy.api;

import java.util.List;

@FunctionalInterface
public interface AnswerProvider<T> {
  public T provide(List<? extends Object> args);
}
