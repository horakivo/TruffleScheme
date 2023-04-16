package com.ihorak.truffle.builtin.comperison;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsNullExprNodeTest {
    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenEmptyList_whenCalled_thenTrueIsReturned() {
        var program = "(null? (list))";

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }


    @Test
    public void givenNonEmptyList_whenCalled_thenFalseIsReturned() {
        var program = "(null? (list 1 2 3))";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }


    @Test
    public void givenNumber_whenCalled_thenFalseIsReturned() {
        var program = "(null? 5)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenWrongNumberOfArgs_whenCalled_thenExceptionIsThrown() {
        var program = "(null? 1 2)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                null?: arity mismatch; Expected number of arguments does not match the given number
                expected: 1
                given: 2""", msg);
    }
}
