package com.roscopeco.moxy.api;

/**
 * <p>Implementations of this interface allow mocks to be stubbed to
 * throw or return given values. They are returned by the
 * {@link MoxyEngine#when(java.util.function.Supplier)} method.</p>
 * 
 * <p>Individual engines will usually provide their own implementation
 * of this interface, as stubbing will require internal knowledge 
 * of the engine's mocking strategy.</p>
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 *
 * @param <T> The type this stubber handles.
 */
public interface MoxyStubber<T> {
  public MoxyStubber<T> thenReturn(T object);
  public MoxyStubber<T> thenThrow(Throwable throwable);
}
