package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.parser.Reader;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LengthExprNodeTest {

    @Test
    public void givenListOfNumbers_whenLength_thenReturnSizeOfTheList() {
        var program = "(length (list 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertEquals(2L, result);
    }

    @Test(expected = SchemeException.class)
    public void givenPair_whenLength_thenExceptionShouldBeThrown() {
        var program = "(length (cons 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertEquals(2L, result);
    }

    @Test(expected = UnsupportedSpecializationException.class)
    public void givenNumber_whenLength_thenExceptionShouldBeThrown() {
        var program = "(length 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertEquals(2L, result);
    }

}
