package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CdrExprNodeTest {

    @Test
    public void givenList_whenCdr_thenReturnSecondElementOfList() {
        var program = "(cdr (list 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(2L, SchemeCell.EMPTY_LIST);

        assertEquals(expectedResult, result);
        assertEquals("(2)", result.toString());
    }

    @Test
    public void givenPair_whenCdr_thenReturnSecondElementOfPair() {
        var program = "(cdr (cons 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertEquals(2L, result);
    }

    @Test(expected = UnsupportedSpecializationException.class)
    public void givenNumber_whenCdr_thenUnsupportedSpecializationExceptionShouldBeThrown() {
        var program = "(cdr 1)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }

    @Test(expected = SchemeException.class)
    public void givenEmptyList_whenCdr_thenSchemeExceptionShouldBeThrown() {
        var program = "(cdr (list))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }
}
