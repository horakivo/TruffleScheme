package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CurrentMillisBuiltinNodeTest {

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
    public void givenWrongNumberOfArgs_whenCalled_thenExceptionIsThrown() {
        var program = "(current-milliseconds 1)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                current-milliseconds: arity mismatch; Expected number of arguments does not match the given number
                expected: 0
                given: 1""", msg);
    }
}
