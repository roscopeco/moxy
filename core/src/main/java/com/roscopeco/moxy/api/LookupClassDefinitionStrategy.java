package com.roscopeco.moxy.api;

import java.lang.invoke.MethodHandles;

/**
 * <p>A {@link ClassDefinitionStrategy} implementation that uses a supplied <code>MethodHandles.Lookup</code>.
 * in order to define classes.</p>
 *
 * <p>This strategy ignores the <code>loader</code> parameter to the <code>defineClass</code> method.</p>
 */
public class LookupClassDefinitionStrategy implements ClassDefinitionStrategy {
    private final MethodHandles.Lookup lookup;

    public LookupClassDefinitionStrategy(final MethodHandles.Lookup lookup) {
        this.lookup = lookup;
    }

    @Override
    public <T> Class<T> defineClass(final ClassLoader loader, final Class<T> originalClass, final byte[] code) {
        try {
            @SuppressWarnings("unchecked")
            final var clz = (Class<T>) MethodHandles.privateLookupIn(originalClass, this.lookup).defineClass(code);
            return clz;
        }
        catch (final IllegalAccessException e) {
            throw new MockGenerationException("Unable to define mock class", e);
        }
    }
}
