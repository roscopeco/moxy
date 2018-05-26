package com.roscopeco.moxy.matchers;

class StartsWithMatcher extends SimpleObjectMatcher<String> {
  public StartsWithMatcher(final String string) {
    super(string, false);
  }

  @Override
  public boolean matches(String arg) {
    if (arg == null) {
      return false;
    } else {
      return arg.startsWith(getObject());
    }
  }
  
  @Override
  public String toString() {
    return "<startsWith" + super.toString();
  }
}
