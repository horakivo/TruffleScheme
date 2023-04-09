package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IfExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenNumberInTestClause_whenIfExecuted_thenCorrectResultReturned() {
        var program = "(if 11 88)";

        var result = context.eval("scm", program);

        assertEquals(88L, result.asLong());
    }

    @Test
    public void givenTrueInTestClause_whenIfExecuted_thenCorrectResultReturned() {
        var program = "(if #t (+ 1 2))";


        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void givenFalseInTestClause_whenIfExecuted_thenCorrectResultReturned() {
        var program = "(if #f (+ 1 2) (- 2 1))";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void givenFalseInTestClauseAndNoElseBranch_whenIfExecuted_thenNullShouldBeReturned() {
        var program = "(if #f (+ 1 2))";

        var result = context.eval("scm", program);

        assertTrue(result.isNull());
    }


    @Test
    public void schemeListIsEvaluatedToTrue() {
        var program = "(if (list 1) 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void longIsEvaluatedToTrue() {
        var program = "(if 1 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void doubleIsEvaluatedToTrue() {
        var program = "(if 1.1 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void bigIntegerIsEvaluatedToTrue() {
        var program = "(if " + "9223372036854775810" + " 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void stringIsEvaluatedToTrue() {
        var program = "(if \"aaa\" 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }


    @Test
    public void schemeSymbolIsEvaluatedToTrue() {
        var program = "(if 'a 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void undefinedValueIsEvaluatedToTrue() {
        var program = "(if (if #f 1) 1)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }
}
