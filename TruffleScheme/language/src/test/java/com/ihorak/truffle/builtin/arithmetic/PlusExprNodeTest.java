package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;


public class PlusExprNodeTest {

    @Test
    public void givenSmallNumbers_whenAddThem_thenReturnCorrectResult() {
        var program = "(+ 1 2 3 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(10L, result);
    }

    @Test
    public void givenOneNumber_whenAddIsCalled_thenSameValueShouldBeReturned() {
        var program = "(+ 88)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(88L, result);
    }

    @Test
    public void givenNoNumber_whenAddIsCalled_thenZeroShouldBeReturned() {
        var program = "(+)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(0L, result);
    }

    @Test
    public void givenFloatingDecimalNumber_whenAddIsCalled_thenShouldReturnResult() {
        var program = "(+ 12.3 5.3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(17.6D, result);
    }


    @Test
    public void givenBigNumber_whenAddThem_thenOverflowShouldOccurAndBigIntShouldBeReturned()  {
        var program = "(+ 1 2 " + Long.MAX_VALUE + ")" ;
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger("3")), result);
    }

    @Test
    public void givenNumbersBiggerThenLong_whenAddThem_thenBigIntShouldBeReturned()  {
        var program = "(+ 1 2 " + BigInteger.TWO.add(BigInteger.valueOf(Long.MAX_VALUE)) + ")" ;
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).add(new BigInteger("5")), result);
    }
}