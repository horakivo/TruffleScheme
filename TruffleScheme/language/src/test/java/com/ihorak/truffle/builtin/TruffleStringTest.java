package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TruffleStringTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenSimpleString_whenEvaluated_stringShouldBeReturned() {
        var program = """
                "hello world"
                """;

        var result = context.eval("scm", program);

        assertEquals("hello world", result.asString());
    }
}
