package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LetStarExprNodeTest {
    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSimpleLetStar_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(let* ((x 10)\n" +
                "       (y (* x x))\n" +
                "       (z (- y x)))\n" +
                "  (+ z y))";

        var result = context.eval("scm", program);

        assertEquals(190L, result.asLong());
    }

    @Test
    public void givenSimpleLetStarWithValueFromGlobal_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(define x 100)" +
                "(let* ((x (+ x 10))\n" +
                "       (y (* x x))\n" +
                "       (z (- y x)))\n" +
                "  (+ z y))";

        var result = context.eval("scm", program);

        assertEquals(24090L, result.asLong());
    }
}
