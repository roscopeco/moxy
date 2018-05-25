package com.roscopeco.moxy.matchers;

import java.util.Deque;

/**
 * <p>A matcher provides argument-matching for Moxy mocks. These
 * are used during both stubbing and verification to allow flexibility
 * in matching arguments.</p>
 * 
 * <p>There are some caveats to bear in mind when using matchers.
 * See README.md for more details and a guide on using matchers 
 * correctly.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 *
 * @param <T> The type of argument this matcher handles.
 */
public interface MoxyMatcher<T> {
  /**
   * <p>Called by the framework to determine whether the given argument
   * should be considered a match for this matcher.</p>
   * 
   * @param arg The method argument
   * 
   * @return <code>true</code> if the argument matches, <code>false</code> otherwise.
   */
  public boolean matches(T arg);
  
  /**
   * <p>Called by the framework when loading this matcher to the internal
   * stack. A default implementation is provided that correctly handles
   * the majority of cases (i.e. simply pushing to the stack).</p>
   * 
   * <p>For more complex matchers, this method can be overridden to
   * manipulate the stack as required.</p>
   * 
   * <p><strong>Note</strong> this method surfaces implementation details
   * into the API, and may be removed at some point in the near future.</p>
   * 
   * @param stack The internal matcher stack.
   */
  // TODO don't like this, it exposes impl details.
  //      Maybe matchers should be made internal, and the engine handles it all.
  //      Or have an internal wrapper...
  //      Or something.
  default public void addToStack(Deque<MoxyMatcher<?>> stack) {
    stack.push(this);    
  }
}
