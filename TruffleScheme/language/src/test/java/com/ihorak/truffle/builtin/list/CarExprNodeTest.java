package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CarExprNodeTest {

    @Test
    public void givenList_whenCar_thenReturnFirstElementOfList() {
        var program = "(car (list 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertEquals(1L, result);
    }

    @Test
    public void givenFirstElementList_whenCar_thenReturnFirstElementOfList() {
        var program = "(car (list (list 1) 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(1L, SchemeCell.EMPTY_LIST);

        assertEquals(expectedResult, result);
    }

    @Test
    public void givenPair_whenCar_thenReturnFirstElementOfPair() {
        var program = "(car (cons 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertEquals(1L, result);
    }

    @Test(expected = UnsupportedSpecializationException.class)
    public void givenNumber_whenCar_thenUnsupportedSpecializationExceptionShouldBeThrown() {
        var program = "(car 1)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }

    @Test(expected = SchemeException.class)
    public void givenEmptyList_whenCar_thenSchemeExceptionShouldBeThrown() {
        var program = "(car (list))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }
}
