package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class MultiplyExprNodeTest {
    @Test
    public void givenSmallNumbers_whenMultiplyThem_thenReturnCorrectResult() {
        var program = "(* 1 2 3 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(24L, result);
    }

    @Test
    public void givenOneNumber_whenMultiplied_thenSameValueShouldBeReturned() {
        var program = "(* 88)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(88L, result);
    }

    @Test
    public void givenNoNumber_whenMultiplyCalled_thenOneShouldBeReturned() {
        var program = "(*)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(1L, result);
    }


    @Test
    public void givenBigNumber_whenMultiplied_thenOverflowShouldOccurAndBigIntShouldBeReturned()  {
        var program = "(* 1 2 " + Long.MAX_VALUE + ")" ;
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).multiply(new BigInteger("2")), result);
    }

    @Test
    public void givenNumbersBiggerThenLong_whenAddThem_thenBigIntShouldBeReturned()  {
        var program = "(* 1 2 " + BigInteger.TWO.add(BigInteger.valueOf(Long.MAX_VALUE)) + ")" ;
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.TWO).multiply(BigInteger.TWO), result);
    }
}
