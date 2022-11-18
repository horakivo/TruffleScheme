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
    public void aaaa() {
        var program = "((lambda (x) (define y 10) (+ x y)) 10)";


        var result =  context.eval("scm", program);

        assertEquals(12.3D + 5.3 + 1.1, result.asDouble(), 0);
    }

//    @Test
//    public void givenBigNumber_whenAddThem_thenOverflowShouldOccurAndBigIntShouldBeReturned() {
//        var program = "(+ 1 2 " + Long.MAX_VALUE + ")";
//
//
//        var result =  context.eval("scm", program);
//
//        assertEquals(new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger("3")), result);
//    }
//
//    @Test
//    public void givenNumbersBiggerThenLong_whenAddThem_thenBigIntShouldBeReturned() {
//        var program = "(+ 1 2 " + BigInteger.TWO.add(BigInteger.valueOf(Long.MAX_VALUE)) + ")";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).add(new BigInteger("5")), result);
//    }
}
