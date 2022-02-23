package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinusExprNodeTest {

    @Test
    public void givenSmallNumbers_whenAreSubtracted_thenCorrectResultShouldBeReturned() {
        var program = "(- 1 2 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(-4L, result);
    }

    @Test(expected = SchemeException.class)
    public void givenNoNumber_whenMinusIsCalled_thenExceptionShouldBeThrown() {
        var program = "(-)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }

    @Test
    public void givenOnePositiveNumber_whenMinusIsCalled_thenNumberShouldBeNegative() {
        var program = "(- 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(-5L, result);
    }

    @Test
    public void givenOneNegativeNumber_whenMinusIsCalled_thenNumberShouldBePositive() {
        var program = "(- -5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(5L, result);
    }

    /*For (now) simplicity we ignore overflow*/
//    @Test
//    public void givenBigNumbers_whenMinusIsCalled_thenBigIntShouldBeReturned() throws IOException, RecognitionException {
//        var program = "(- " + Long.MIN_VALUE + " 2 3)";
//        var expr = Reader.read(new ByteArrayInputStream(program.getBytes()));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(new BigInteger(String.valueOf(Long.MIN_VALUE)).subtract(new BigInteger("2")).subtract(new BigInteger("3")), result);
//    }
}
