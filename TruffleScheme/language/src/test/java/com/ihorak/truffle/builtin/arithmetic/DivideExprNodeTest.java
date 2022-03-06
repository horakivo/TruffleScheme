package com.ihorak.truffle.builtin.arithmetic;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DivideExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSmallNumber_whenDivideCalled_thenOneDividedByTheNumberShouldBeReturned() {
        var program = "(/ 4)";

        var result = context.eval("scm", program);

        assertEquals(1 / 4D, result.asDouble(), 0);
    }

    @Test
    public void givenNoNumber_whenDivideCalled_thenSchemeExceptionShouldBeThrown() {
        var program = "(/)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("/: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: at least 1\n" +
                "given: 0", msg);

    }

    @Test
    public void givenNumbers_whenDivideCalled_thenCorrectAnswerShouldBeReturn() {
        var program = "(/ 2 3 4)";


        var result = context.eval("scm", program);

        assertEquals(2 / 3D / 4, result.asDouble(), 0);
    }
}
