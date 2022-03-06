package com.ihorak.truffle.builtin.arithmetic;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class MultiplyExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSmallNumbers_whenMultiplyThem_thenReturnCorrectResult() {
        var program = "(* 1 2 3 4)";


        var result = context.eval("scm", program);

        assertEquals(24L, result.asLong());
    }

    @Test
    public void givenOneNumber_whenMultiplied_thenSameValueShouldBeReturned() {
        var program = "(* 88)";

        var result = context.eval("scm", program);

        assertEquals(88L, result.asLong());
    }

    @Test
    public void givenNoNumber_whenMultiplyCalled_thenOneShouldBeReturned() {
        var program = "(*)";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }


//    @Test
//    public void givenBigNumber_whenMultiplied_thenOverflowShouldOccurAndBigIntShouldBeReturned() {
//        var program = "(* 1 2 " + Long.MAX_VALUE + ")";
//
//
//        var result = context.eval("scm", program);
//
//        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).multiply(new BigInteger("2")), result);
//    }

//    @Test
//    public void givenNumbersBiggerThenLong_whenAddThem_thenBigIntShouldBeReturned() {
//        var program = "(* 1 2 " + BigInteger.TWO.add(BigInteger.valueOf(Long.MAX_VALUE)) + ")";
//
//        var result = context.eval("scm", program);
//
//        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.TWO).multiply(BigInteger.TWO), result);
//    }
}
