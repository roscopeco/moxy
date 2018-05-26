package com.roscopeco.moxy.matchers;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

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
  
  private final int expectedSize;
  private final Deque<MoxyMatcher<?>> stack;

  public InconsistentMatchersException(final int expectedSize, 
                                       final Deque<MoxyMatcher<?>> stack) {
    super("Inconsistent use of matchers: if using matchers, *all* arguments must be supplied with them.\n"
        + "This limitation will (hopefully) be lifted in the future.\n\n"
        + "Expected size: " + expectedSize + "; actual: " + stack.size() + "\n"
        + "Stack: [" 
        + stack.stream().map(MoxyMatcher::toString).collect(Collectors.joining(", ")) 
        + "]");
    
    this.expectedSize = expectedSize;
    this.stack = new ArrayDeque<>(stack);
  }

  int getExpectedSize() {
    return expectedSize;
  }

  int getActualSize() {
    return stack.size();
  }

  Deque<MoxyMatcher<?>> getStack() {
    return stack;
  }
}
