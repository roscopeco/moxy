package com.roscopeco.moxy.matchers;

class EndsWithMatcher extends SimpleObjectMatcher<String> {
  public EndsWithMatcher(final String string) {
    super(string, false);
  }

  @Override
  public boolean matches(String arg) {
    if (arg == null) {
      return false;
    } else {
      return arg.endsWith(getObject());
    }
  }
  
  @Override
  public String toString() {
    return "<endsWith" + super.toString();
  }
}
