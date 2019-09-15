package com.roscopeco.moxy.regression;

import com.roscopeco.moxy.Moxy;
import org.junit.jupiter.api.Test;

class TestRegressionMoxy13 {
    public interface Super {
        void superMethod();
    }

    public interface Sub extends Super {
        void subMethod();
    }

    @Test
    void testMoxyBug() {
        final Sub sub = Moxy.mock(Sub.class);

        sub.subMethod();        // Works fine
        sub.superMethod();      // AbstractMethodError is thrown; Output shows superMethod was *not* generated
    }
}