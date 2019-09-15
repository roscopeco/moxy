package com.roscopeco.moxy.api;

/**
 * <p>A class definition strategy is used to actually define a mock class.</p>
 *
 * <p>The default implementation of this class uses reflective access to the
 * <code>defineClass(String, byte[], int, int, ProtectionDomain)</code> method
 * on <code>java.lang.ClassLoader</code>.</p>
 *
 * <p>There is an alternative implementation that uses a client-supplied <code>MethodHandles.Lookup</code>
 * to define the class. This will probably become the default under future Java versions.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @see LookupClassDefinitionStrategy
 * @since 1.0
 */
public interface ClassDefinitionStrategy {
    /**
     * Define a class in the manner supported by this strategy.
     *
     * @param targetLoader  Desired Target ClassLoader. Not all implementations must support this.
     * @param originalClass The original class.
     * @param mockCode      The bytecode for the mock.
     * @param <T>           The type of the class being mocked.
     * @return A newly-defined class.
     */
    <T> Class<T> defineClass(ClassLoader targetLoader, Class<T> originalClass, byte[] mockCode);
}
