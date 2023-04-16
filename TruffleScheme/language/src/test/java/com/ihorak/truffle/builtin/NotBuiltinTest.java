package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotBuiltinTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @After
    public void tearDown() {
        this.context.close();
    }

    @Test
    public void givenFalse_whenNegated_thenTrueIsReturned() {
        var program = """
                (not #f)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTrue_whenNegated_thenFalseIsReturned() {
        var program = """
                (not #t)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenEmptyList_whenNegated_thenFalseIsReturned() {
        var program = """
                (not '())
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenNumber_whenNegated_thenFalseIsReturned() {
        var program = """
                (not 6)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }


    @Test
    public void givenProcedure_whenNegated_thenFalseIsReturned() {
        var program = """
                (not (lambda (n) n))
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenPrimitiveProcedure_whenNegated_thenFalseIsReturned() {
        var program = """
                (not +)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }
}
