package com.roscopeco.moxy.regression;

import com.roscopeco.moxy.Moxy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestRegressionMoxy11 {
    public static class ClassWithNested {
        static class Inner {
            private int theField;
        }

        public Inner test() {
            Inner inner = new Inner();
            inner.theField = 5;
            return inner;
        }
    }

    @Test
    void testMoxyBug11() {
        ClassWithNested.Inner inner = Moxy.mock(ClassWithNested.Inner.class);   /* NPE Here */
        assertThat(inner).isNotNull();
    }
}
