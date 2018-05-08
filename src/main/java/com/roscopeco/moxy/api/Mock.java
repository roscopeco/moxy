package com.roscopeco.moxy.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * All mocks should have this annotation.
 * 
 * @author Ross.Bamford
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Mock {
  
}
