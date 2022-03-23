package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AndExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenTwoArgs_whenEvaluated_thenReturnCorrectResult() {
        var program = "(and 1 2)";

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }

    @Test
    public void givenOneArg_whenEvaluated_thenReturnCorrectResult() {
        var program = "(and 3)";

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void givenNoArg_whenEvaluated_thenTrueIsReturned() {
        var program = "(and)";

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenSimpleArbitraryNumberOfArgs_whenEvaluated_thenCorrectValueIsReturned() {
        var program = "(and 1 2 3 4 5 6 7 8)";

        var result = context.eval("scm", program);

        assertEquals(8L, result.asLong());
    }

    @Test
    public void givenSimpleArbitraryNumberOfArgsWithFalse_whenEvaluated_thenFalseIsReturned() {
        var program = "(and 1 2 3 #f adad 6 7 8)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenNoDefinedValue_whenEvaluated_thenErrorIsThrown() {
        var program = "(and 1 2 3 adad 6 7 8)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("'adad: undefined\n" +
                "cannot reference an identifier before its definition", msg);
    }
}
