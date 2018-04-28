package com.roscopeco.moxy.internal;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Automatically added to mocks so others can be sure
 * they're working with a mock.
 * 
 * @author Ross.Bamford
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface IsMock {
  
}
