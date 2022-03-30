package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class CondTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenNoArgs_whenEvaluated_thenUndefinedValueIsReturned() {
        var program = "(cond)";

        var result = context.eval("scm", program);

        assertTrue(result.isNull());
        assertEquals("undefined", result.toString());
    }

    @Test
    public void givenSimpleCondWithoutElse_whenEvaluated_thenCorrectResultIsReturned() {
        var program = "(cond (#f 20) (5 20))";

        var result = context.eval("scm", program);

        assertEquals(20L, result.asLong());
    }

    @Test
    public void givenSimpleCondWithElse_whenEvaluated_thenCorrectResultIsReturned() {
        var program = "(cond (#f 20) (else 22))";

        var result = context.eval("scm", program);

        assertEquals(22L, result.asLong());
    }

    @Test
    public void givenCondWithElse_whenEvaluated_thenCorrectResultIsReturned() {
        var program = "(cond ((= 1 2) 20) ((= 3 2) 20) (else 22))";

        var result = context.eval("scm", program);

        assertEquals(22L, result.asLong());
    }

    @Test
    public void givenCondWithoutElse_whenEvaluated_thenCorrectResultIsReturned() {
        var program = "(cond ((= 1 2) 20) ((= 2 2) 20))";

        var result = context.eval("scm", program);

        assertEquals(20L, result.asLong());
    }

    @Test
    public void givenCond_whenEvaluated_thenShouldFollowShortCircuitImplementation() {
        var program = "(cond ((= 2 2) 20) (undefined 20))";

        var result = context.eval("scm", program);

        assertEquals(20L, result.asLong());
    }


    @Test
    public void givenWrongSyntax_whenEvaluated_thenErrorShouldBeThrown() {
        var program = "(cond 5)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("" +
                "cond: bad syntax\n" +
                "expected: list?\n" +
                "given: 5",msg);
    }

    @Test
    public void givenWrongSyntax2_whenEvaluated_thenErrorShouldBeThrown() {
        var program = "(cond (4 5) randomStuff)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("" +
                "cond: bad syntax\n" +
                "expected: list?\n" +
                "given: 'randomStuff",msg);
    }
}
