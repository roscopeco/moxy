module com.roscopeco.moxy.core {
    exports com.roscopeco.moxy;
    exports com.roscopeco.moxy.api;

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