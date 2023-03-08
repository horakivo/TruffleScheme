package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EvalSourceTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void test() {
        var program = """
                (eval-source "scm" "(+ 1 1)")
                """;

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }
}
