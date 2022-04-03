package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenRandomNumber_whenEvaluated_thenReturnCorrectResult() {
        var program = "(eval 5 6 7 8 9)";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

}
