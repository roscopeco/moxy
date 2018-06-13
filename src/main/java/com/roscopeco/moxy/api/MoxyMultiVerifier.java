package com.roscopeco.moxy.api;

public interface MoxyMultiVerifier {
  public void wereNotCalled();

  public MoxyMultiVerifier wereAllCalled();
  public MoxyMultiVerifier wereAllCalledExactly(int expectedTimes);
  public MoxyMultiVerifier wereAllCalledOnce();
  public MoxyMultiVerifier wereAllCalledTwice();
  public MoxyMultiVerifier wereAllCalledAtLeast(int expectedTimes);
  public MoxyMultiVerifier wereAllCalledAtLeastTwice();
  public MoxyMultiVerifier wereAllCalledAtMost(int expectedTimes);
  public MoxyMultiVerifier wereAllCalledAtMostTwice();

  public void inThatOrder();
  public void exclusivelyInThatOrder();
  public void inAnyOrder();
}
