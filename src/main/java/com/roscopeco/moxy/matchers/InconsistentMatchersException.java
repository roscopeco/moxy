package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

/**
 * <p>Thrown to indicate that the matcher stack was found to be
 * in an inconsistent state.</p>
 * 
 * <p>This usually indicates that matchers have been mixed with
 * immediate value arguments in a stub or verify operation, or
 * that a matcher method has been called in an improper context
 * (i.e. not in a stub or verify operation).</p>
 * 
 * <p>When this exception is thrown, the stack is also reset to
 * attempt to provide a consistent environment for future use.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class InconsistentMatchersException extends MoxyException {
  private static final long serialVersionUID = 1L;

  public InconsistentMatchersException() {
    super("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
        + "This limitation will (hopefully) be lifted in the future.");
  }
}
