/**
 * <p>Moxy is a type-safe mocking/spying framework for Java with a fluent API
 * and built-in support for advanced features.</p>
 *
 * <p>The easiest way to get started is to use the {@link com.roscopeco.moxy.Moxy}
 * class (follow the link, there's some examples there too).</p>
 *
 * <p>This module provides the core functionality for the framework, providing
 * the ability to create mocks, set them up for testing, and verify that they
 * were interacted with as per your expectations.</p>
 *
 * <p>It takes inspiration from a number of other popular mocking frameworks,
 * notably Mockito. It aims to be lean, fast, and as far as possible to lack
 * the "surprises" one finds in other mocking frameworks (e.g. it should not
 * give weird failures in unrelated tests, but instead should fail fast when
 * used improperly).</p>
 */
module com.roscopeco.moxy.core {
    exports com.roscopeco.moxy;
    exports com.roscopeco.moxy.api;
    exports com.roscopeco.moxy.matchers;

    requires java.instrument;
    requires java.logging;

    requires jdk.unsupported;

    requires net.bytebuddy.agent;

    requires org.apache.commons.lang3;

    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.objectweb.asm.util;
    requires org.objectweb.asm.commons;
    requires org.opentest4j;
}