package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LetrecTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSimpleLet_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(letrec\n" +
                "    ((x 10)\n" +
                "     (y x))\n" +
                "  (+ x y))";

        var result = context.eval("scm", program);

        assertEquals(20L, result.asLong());
    }
}
