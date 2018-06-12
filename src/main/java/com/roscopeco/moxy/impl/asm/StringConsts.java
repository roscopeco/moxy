package com.roscopeco.moxy.impl.asm;

enum StringConsts {
  SPACE(" "),
  COMMA_SPACE(", "),
  LSQPAREN("["),
  RSQPAREN("]"),
  EXPECTED_MOCK("Expected mock "),
  TO_BE_CALLED(" to be called "),
  NEVER_TO_THROW_EXCEPTION("never to throw exception "),
  BUT_IT_WAS_CALLED(", but it was called "),
  BUT_IT_WAS_THROWN(", but it was thrown "),
  NEVER_THROW_ANY_BUT_EXCEPTIONS_THROWN("never to throw any exception, but exceptions were thrown "),
  EXACTLY("exactly"),
  AT_LEAST("at least"),
  AT_MOST("at most"),
  AT_LEAST_ONCE_BUT_WASNT_AT_ALL("at least once but it wasn't called at all"),
  ;

  private final String value;

  private StringConsts(final String value) {
    this.value = value;
  }

  String value() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value();
  }
}
