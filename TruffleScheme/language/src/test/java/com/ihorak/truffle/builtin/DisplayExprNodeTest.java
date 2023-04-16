package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DisplayExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void displayDoesNotThrowExceptionAndReturnsUndefined() {
        var program = """
                (display "hello world")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.isNull());
        assertEquals("undefined", result.toString());
    }

    @Test
    public void givenWrongNumberOfArgs_whenCalled_thenExceptionIsThrown() {
        var program = """
                (display "hello world" 5)
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                display: arity mismatch; Expected number of arguments does not match the given number
                expected: 1
                given: 2""", msg);
    }
}
