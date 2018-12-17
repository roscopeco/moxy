package com.roscopeco.moxy;

import static com.roscopeco.moxy.Moxy.assertMocks;
import static com.roscopeco.moxy.Moxy.mock;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;

class RegressionExclusiveOrderDoesntWork {
    @Test
    void exclusiveOrderUnclearBugExample() {
        ClassWithPrimitiveReturns c = mock(ClassWithPrimitiveReturns.class);

        c.returnBoolean();
        c.returnByte();
        c.returnInt();
        c.returnChar();
        c.returnDouble();

        assertThatThrownBy(() -> assertMocks(() -> {
            c.returnBoolean();
            c.returnByte();
            c.returnChar();
            c.returnDouble();
        })
            .wereAllCalledOnce()
            .exclusivelyInThatOrder())
            .isInstanceOf(AssertionFailedError.class)
            .hasMessageContaining("extra invocations");
    }
}
