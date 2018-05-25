package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

/**
 * <p>Thrown to indicate that one or more matchers are in an
 * incorrect state. This often indicates that an incorrect-typed
 * matcher method has been used (usually with a primitive argument).</p>
 *  
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
public class IllegalMatcherStateException extends MoxyException {
  private static final long serialVersionUID = 1L;
  
  public IllegalMatcherStateException(String message) {
    super(message);
  }
}
