package com.roscopeco.moxy.impl.asm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

import com.roscopeco.moxy.api.ClassDefinitionStrategy;
import com.roscopeco.moxy.api.MockGenerationException;
import com.roscopeco.moxy.api.MoxyException;

/**
 * This is the default {@link ClassDefinitionStrategy} used by the ASM implementation.
 */
public class DefaultClassDefinitionStrategy implements ClassDefinitionStrategy {
    private final MethodHandle defineClass;

    public DefaultClassDefinitionStrategy() {
        try {
            final var method = ClassLoader.class.getDeclaredMethod(
                "defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
            method.setAccessible(true);

            this.defineClass = MethodHandles.lookup().unreflect(method);
        }
        catch (final NoSuchMethodException | IllegalAccessException e) {
            throw new MoxyException("Unable to initialize default strategy; java.lang.ClassLoader not compatible", e);
        }
    }

    @Override
    public <T> Class<T> defineClass(final ClassLoader loader, final Class<T> originalClass, final byte[] code) {
        if (loader == null) {
            throw new IllegalArgumentException(
                "Implicit definition in the system classloader is unsupported.\n"
                    + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
                    + "If you're sure this is what you want to do, pass system loader explicitly (rather than null)");
        }

        try {
            @SuppressWarnings("unchecked")
            final var clz = (Class<T>) this.defineClass.invoke(loader, null, code, 0, code.length, null);
            return clz;
        }
        catch (final IllegalAccessException e) {
            throw new MockGenerationException("Unable to define mock class", e);
        }
        catch (final InvocationTargetException e) {
            throw new MockGenerationException("Supplied class loader is not compatible", e);
        }
        catch (final Throwable t) {
            throw new MockGenerationException("Exception during class definition", t);
        }
    }
}
