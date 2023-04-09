package com.ihorak.truffle.builtin.arithmetic;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ModuloExprNodeTest {

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
    public void givenInteger_whenExecuted_correctResultReturned() {
        var program = "(modulo 3 2)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void givenDouble_whenExecuted_thenExceptionIsThrown() {
        var program = "(modulo 3.1 2)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                modulo: contract violation
                expected: integer?
                given left: 3.1
                given right: 2""", msg);
    }

    @Test
    public void givenBigInteger_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(modulo 3 " + "9223372036854775810" + " )";


        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

}
