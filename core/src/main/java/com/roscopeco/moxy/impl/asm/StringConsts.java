/*
 * Moxy - Lean-and-mean mocking framework for Java with a fluent API.
 *
 * Copyright 2018 Ross Bamford
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included
 *   in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.roscopeco.moxy.impl.asm;

enum StringConsts {
    SPACE(" "),
    COMMA_SPACE(", "),
    LSQPAREN("["),
    RSQPAREN("]"),
    EXPECTED_MOCK("Expected mock "),
    TO_BE_CALLED(" to be called "),
    NEVER_TO_THROW_EXCEPTION("never to throw exception "),
    BUT_IT_WAS_CALLED(", but it was called "),
    BUT_IT_WAS_THROWN(", but it was thrown "),
    NEVER_THROW_ANY_BUT_EXCEPTIONS_THROWN("never to throw any exception, but exceptions were thrown "),
    EXACTLY("exactly"),
    AT_LEAST("at least"),
    AT_MOST("at most"),
    AT_LEAST_ONCE_BUT_WASNT_AT_ALL("at least once but it wasn't called at all"),
    TAB("\t"),
    EOL("\n"),
    EXPECTED_INVOCATIONS("Expected invocations:"),
    EXCLUSIVELY("exclusively "),
    IN_ORDER_BUT_WERE("in that order, but they were "),
    NOT_INVOKED_OR("not invoked or were "),
    OUT_OF_ORDER("invoked out of order"),
    ;

    private final String value;

    StringConsts(final String value) {
        this.value = value;
    }

    String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
