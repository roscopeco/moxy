package com.roscopeco.moxy.matchers;

import java.util.regex.Pattern;

class RegexMatcher extends SimpleObjectMatcher<CharSequence> {
  private final Pattern regex;
  
  public RegexMatcher(final String regex) {
    super(regex, false);
    this.regex = Pattern.compile(regex);
  }

  @Override
  public boolean matches(CharSequence arg) {
    if (arg == null) {
      return false;
    } else {
      return regex.matcher(arg).find();     
    }
  }
  
  @Override
  public String toString() {
    return "<regex: " + regex.toString() + ">";
  }
}
