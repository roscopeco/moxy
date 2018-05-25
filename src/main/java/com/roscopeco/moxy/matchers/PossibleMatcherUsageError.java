package com.roscopeco.moxy.matchers;

import com.roscopeco.moxy.api.MoxyException;

/**
 * <p>Thrown to indicate that Moxy has detected potentially-incorrect
 * usage of matchers. This will provide a helpful message to the
 * user, rather than a random, hard-to-debug crash at some 
 * indeterminate future time.</p>
 * 
 * <p>Note that this doesn't <em>always</em> indicate incorrect 
 * usage - but it does <em>usually</em> indicate it.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 *
 */
public class PossibleMatcherUsageError extends MoxyException {
  private static final long serialVersionUID = 1L;
  
  public PossibleMatcherUsageError(String message, Throwable cause) {
    super(message, cause);
  }
}
