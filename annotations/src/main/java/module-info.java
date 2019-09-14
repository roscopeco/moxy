module com.roscopeco.moxy.annotations {
    exports com.roscopeco.moxy.annotations;
    exports com.roscopeco.moxy.annotations.junit5;

    requires com.roscopeco.moxy.core;

    requires org.junit.jupiter.api;
}