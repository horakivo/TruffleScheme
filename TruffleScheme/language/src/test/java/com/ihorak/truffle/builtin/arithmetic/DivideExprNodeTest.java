package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DivideExprNodeTest {

    @Test
    public void givenSmallNumber_whenDivideCalled_thenOneDividedByTheNumberShouldBeReturned() {
        var program = "(/ 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(1 / 4D, result);
    }

    @Test
    public void givenNoNumber_whenDivideCalled_thenSchemeExceptionShouldBeThrown() {
        var program = "(/)";

        var msg = assertThrows(SchemeException.class, () ->  Reader.readExpr(CharStreams.fromString(program))).getMessage();

        assertEquals("/: arity mismatch; Expected number of arguments does not match the given number \n" +
                " expected: at least 1 \n" +
                " given: 0", msg);

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
