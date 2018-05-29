package com.roscopeco.moxy.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * All mocks created by a {@link MoxyEngine} implementation 
 * <strong>must</strong> have this annotation.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Mock {
  
}
