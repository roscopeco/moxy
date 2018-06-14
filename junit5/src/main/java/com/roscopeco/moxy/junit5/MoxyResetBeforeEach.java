package com.roscopeco.moxy.junit5;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.roscopeco.moxy.Moxy;

public class MoxyResetBeforeEach implements BeforeEachCallback {
  @Override
  public void beforeEach(final ExtensionContext context) throws Exception {
    Moxy.getMoxyEngine().reset();
  }
}
