package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuoteExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenIfExpr_whenQuoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quote (if (+ 3 4) 5 4))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("if", result.getArrayElement(0).asString());
        assertEquals("+", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals(3L, result.getArrayElement(1).getArrayElement(1).asLong());
        assertEquals(4L, result.getArrayElement(1).getArrayElement(2).asLong());
        assertEquals(5L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenLambdaExpr_whenQuoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quote (lambda (x y) (+ 1 2) #f))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("lambda", result.getArrayElement(0).asString());
        assertEquals("x", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals("y", result.getArrayElement(1).getArrayElement(1).asString());
        assertEquals("+", result.getArrayElement(2).getArrayElement(0).asString());
        assertEquals(1L, result.getArrayElement(2).getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).getArrayElement(2).asLong());
        assertFalse(result.getArrayElement(3).asBoolean());
    }

    @Test
    public void givenRandomList_whenQuoteAsTickIsExecuted_thenListAsDataShouldBeReturned() {
        var program = "'(1 2 3)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
    }

    @Test
    public void givenLambdaExpr_whenQuoteAsTickIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "'(lambda (x y) (+ 1 2) #f)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("lambda", result.getArrayElement(0).asString());
        assertEquals("x", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals("y", result.getArrayElement(1).getArrayElement(1).asString());
        assertEquals("+", result.getArrayElement(2).getArrayElement(0).asString());
        assertEquals(1L, result.getArrayElement(2).getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).getArrayElement(2).asLong());
        assertFalse(result.getArrayElement(3).asBoolean());
    }

    @Test
    public void givenRandomNumber_whenQuoteAsTickIsExecuted_thenInternalReprOfNumberShouldBeReturned() {
        var program = "'5";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenRandomSymbol_whenQuoteAsTickIsExecuted_thenInternalReprOfNumberShouldBeReturned() {
        var program = "'abc";

        var result = context.eval("scm", program);

        assertEquals("abc", result.asString());
    }

    @Test
    public void givenWrongNumberOfArgs_whenQuoteExecuted_thenThrowParserException() {
        var program = "(quote abc x)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("quote: arity mismatch\n" +
                "expected: 1\n" +
                "given: 2", msg);
    }
}
