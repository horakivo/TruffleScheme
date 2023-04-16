package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenTwoArgs_whenEvaluated_thenReturnCorrectResult() {
        var program = "(or 1 #f)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void givenOneArg_whenEvaluated_thenReturnCorrectResult() {
        var program = "(or 3)";

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void givenOneBooleanArg_whenEvaluated_thenReturnCorrectResult() {
        var program = "(or #f)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenNoArg_whenEvaluated_thenFalseIsReturned() {
        var program = "(or)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenSimpleArbitraryNumberOfArgs_whenEvaluated_thenCorrectValueIsReturned() {
        var program = "(or 1 2 3 4 5 6 7 8)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void givenSimpleArbitraryNumberOfArgsWithFalse_whenEvaluated_thenFalseIsReturned() {
        var program = "(or (< 3 2) #f 5 random)";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenNoDefinedValue_whenEvaluated_thenErrorIsThrown() {
        var program = "(or (< 3 2) (= 4 3) adad 6 7 8)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("'adad: undefined\n" +
                "cannot reference an identifier before its definition", msg);
    }
}
