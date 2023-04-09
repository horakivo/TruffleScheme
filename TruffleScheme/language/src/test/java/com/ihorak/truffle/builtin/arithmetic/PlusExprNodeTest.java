package com.ihorak.truffle.builtin.arithmetic;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;


public class PlusExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSmallNumbers_whenAddThem_thenReturnCorrectResult() {
        var program = "(+ 1 2 3 4 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void givenOneNumber_whenAddIsCalled_thenSameValueShouldBeReturned() {
        var program = "(+ 88)";

        var result = context.eval("scm", program);

        assertEquals(88L, result.asLong());
    }

    @Test
    public void givenNoNumber_whenAddIsCalled_thenZeroShouldBeReturned() {
        var program = "(+)";

        var result = context.eval("scm", program);

        assertEquals(0L, result.asLong());
    }

    @Test
    public void givenFloatingDecimalNumber_whenAddIsCalled_thenShouldReturnResult() {
        var program = "(+ 12.3 5.3 1.1)";


        var result =  context.eval("scm", program);

        assertEquals(12.3D + 5.3 + 1.1, result.asDouble(), 0);
    }

    @Test
    public void givenBigNumber_whenAddThem_thenOverflowShouldOccurAndBigIntShouldBeReturned() {
        var program = "(+ 1 " + Long.MAX_VALUE + ")";

        var result =  context.eval("scm", program);

        // Truffle Interop API doesn't support BigInt in version 22.3.1 (it will in 23.0)
        assertEquals("9223372036854775808", result.toString());
    }

}
