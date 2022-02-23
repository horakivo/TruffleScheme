package com.ihorak.truffle.builtin.logical;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LessThenOrEqualExprNodeTest {

//    @Test
//    public void givenNoArgs_whenExecuted_thenExceptionShouldBeThrown() {
//        var program = "(<=)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var exception = assertThrows(SchemeException.class, () -> expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame()));
//
//        assertEquals("asd", exception.getMessage());
//
//    }

    @Test
    public void givenOneArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(<= 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertTrue((boolean) result);
    }

    @Test
    public void givenTwoArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(<= 3 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertTrue((boolean) result);
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(<= 3 4 5 6 7 8 9 10)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertTrue((boolean) result);
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnFalse() {
        var program = "(<= 3 4 6 5 7 8 9 10)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertFalse((boolean) result);
    }

//    @Test(expected = SchemeException.class)
//    public void givenNoNumber_whenMinusIsCalled_thenExceptionShouldBeThrown() {
//        var program = "(-)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//    }
//
//    @Test
//    public void givenOnePositiveNumber_whenMinusIsCalled_thenNumberShouldBeNegative() {
//        var program = "(- 5)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(-5L, result);
//    }
//
//    @Test
//    public void givenOneNegativeNumber_whenMinusIsCalled_thenNumberShouldBePositive() {
//        var program = "(- -5)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(5L, result);
//    }
}
