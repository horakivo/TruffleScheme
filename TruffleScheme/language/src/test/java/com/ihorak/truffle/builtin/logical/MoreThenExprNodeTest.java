package com.ihorak.truffle.builtin.logical;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoreThenExprNodeTest {
    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenNoArgs_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(>)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals(">: arity mismatch; Expected number of argument does not match the given number\n" +
                "expected: at least 1\n" +
                "given: 0", msg);
    }

    @Test
    public void givenOneArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(> 3)";

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(> 4 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(> 10 9 8 7 6 5 4 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnFalse() {
        var program = "(> 10 9 8 6 7 5 4 3)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenWrongArgumentType_whenExecuted_shouldThrowException() {
        var program = "(> 3 4 'a 5 7 8 9 10)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("" +
                ">: contract violation\n" +
                "expected: real?\n" +
                "given left: 4\n" +
                "given right: 'a", msg);
    }
}
