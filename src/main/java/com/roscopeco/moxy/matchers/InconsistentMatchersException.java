package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

public class InconsistentMatchersException extends MoxyException {
  private static final long serialVersionUID = 1L;

  public InconsistentMatchersException() {
    super("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
        + "This limitation will (hopefully) be lifted in the future.");
  }
}
