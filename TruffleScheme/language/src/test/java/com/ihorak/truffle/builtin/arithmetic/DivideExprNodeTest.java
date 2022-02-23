package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DivideExprNodeTest {

    @Test
    public void givenSmallNumber_whenDivideCalled_thenOneDividedByTheNumberShouldBeReturned() {
        var program = "(/ 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(1 / 4D, result);
    }

    @Test(expected = SchemeException.class)
    public void givenNoNumber_whenDivideCalled_thenSchemeExceptionShouldBeThrown() {
        var program = "(/)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }

    @Test
    public void givenNumbers_whenDivideCalled_thenCorrectAnswerShouldBeReturn() {
        var program = "(/ 2 3 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(2 / 3D / 4, result);
    }
}
