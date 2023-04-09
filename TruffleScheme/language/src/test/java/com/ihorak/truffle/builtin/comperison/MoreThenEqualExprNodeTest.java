package com.ihorak.truffle.builtin.comperison;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class MoreThenEqualExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenNoArgs_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(>=)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals(">=: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 1\n" +
                "given: 0", msg);
    }

    @Test
    public void givenOneArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(>= 3)";

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(>= 4 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoDouble_whenExecuted_thenShouldReturnTrue() {
        var program = "(>= 4.3 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoBigInts_whenExecuted_thenShouldReturnTrue() {
        var left = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.TEN);
        var right = BigInteger.valueOf(Long.MAX_VALUE);
        var program = "(>= " + left + " " + right + ")";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoSameBigInts_whenExecuted_thenShouldReturnTrue() {
        var left = BigInteger.valueOf(Long.MAX_VALUE);
        var right = BigInteger.valueOf(Long.MAX_VALUE);
        var program = "(>= " + left + " " + right + ")";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(>= 10 9 8 7 7 6 5 4 3 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenArbitraryArgWithSameNumbers_whenExecuted_thenShouldReturnTrue() {
        var program = "(>= 3 3 3 3 3 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnFalse() {
        var program = "(>= 10 9 8 6 7 5 4 3)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenWrongArgumentType_whenExecuted_shouldThrowException() {
        var program = "(>= 3 4 'b 5 7 8 9 10)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("" +
                ">=: contract violation\n" +
                "expected: real?\n" +
                "given left: 4\n" +
                "given right: 'b", msg);
    }
}
