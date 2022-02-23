package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

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


    /*For (now) simplicity we ignore overflow*/
//    @Test
//    public void givenNumbersBiggerThenLong_whenAddThem_thenBigIntShouldBeReturned() throws IOException, RecognitionException {
//        var program = "(+ 1 2 " + Long.MAX_VALUE + ")" ;
//        var expr = Reader.read(new ByteArrayInputStream(program.getBytes()));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger("3")), result);
//    }
}
