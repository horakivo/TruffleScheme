package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.convertor.callable.BuiltinConverter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MinusPrimitiveProcedureTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @BeforeClass
    public static void before() {
        BuiltinConverter.isBuiltinEnabled = false;
    }

    @Test
    public void givenSmallNumbers_whenAreSubtracted_thenCorrectResultShouldBeReturned() {
        var program = "(- 1 2 3)";

        var result = context.eval("scm", program);

        assertEquals(-4L, result.asLong());
    }

    @Test
    public void givenNoNumber_whenMinusIsCalled_thenExceptionShouldBeThrown() {
        var program = "(-)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("-: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: at least 1\n" +
                "given: 0", msg);
    }

    @Test
    public void givenOnePositiveNumber_whenMinusIsCalled_thenNumberShouldBeNegative() {
        var program = "(- 5)";

        var result = context.eval("scm", program);

        assertEquals(-5L, result.asLong());
    }

    @Test
    public void givenOneNegativeNumber_whenMinusIsCalled_thenNumberShouldBePositive() {
        var program = "(- -5)";


        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

//    @Test
//    public void givenNegativeBigInt_whenNegated_thenNegatedBigIntShouldBeReturned() {
//        var bigInt = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.TWO).negate();
//        var program = "(- " + bigInt + ")";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(bigInt.negate(), result);
//    }

//    @Test
//    public void givenBigNumbers_whenMinusIsCalled_thenBigIntShouldBeReturned() {
//        var program = "(- " + Long.MIN_VALUE + " 2 3)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(new BigInteger(String.valueOf(Long.MIN_VALUE)).subtract(new BigInteger("2")).subtract(new BigInteger("3")), result);
//    }

    @AfterClass
    public static void after() {
        BuiltinConverter.isBuiltinEnabled = true;
    }
}
