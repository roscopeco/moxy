package com.roscopeco.moxy.junit5;

import com.roscopeco.moxy.Moxy;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MoxyResetBeforeEach implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        Moxy.getMoxyEngine().reset();
        Moxy.resetAllClassMocks();
    }
}
