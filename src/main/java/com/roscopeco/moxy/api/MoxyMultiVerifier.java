package com.roscopeco.moxy.api;

public interface MoxyMultiVerifier {
  public MoxyMultiVerifier inOrder();
  public MoxyMultiVerifier inAnyOrder();
  public MoxyMultiVerifier exclusively();

  public void wereNotCalled();
  public void wereAllCalled();
  public void wereAllCalledExactly(int expectedTimes);
  public void wereAllCalledOnce();
  public void wereAllCalledTwice();
  public void wereAllCalledAtLeast(int expectedTimes);
  public void wereAllCalledAtMost(int expectedTimes);
}
