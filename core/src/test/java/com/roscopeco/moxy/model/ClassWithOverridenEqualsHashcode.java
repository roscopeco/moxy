package com.roscopeco.moxy.model;

public class ClassWithOverridenEqualsHashcode {
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
