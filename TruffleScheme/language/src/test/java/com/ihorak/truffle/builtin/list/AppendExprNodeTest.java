package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppendExprNodeTest {

    @Test
    public void givenTwoList_whenAppend_thenShouldReturnMergedLists() {
        var program = "(append (list 1 2) (list 3 4))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult =
                new SchemeCell(1L,
                        new SchemeCell(2L,
                                new SchemeCell(3L,
                                        new SchemeCell(4L, SchemeCell.EMPTY_LIST))));
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenOneList_whenAppend_thenShouldReturnGivenList() {
        var program = "(append (list 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(1L, new SchemeCell(2L, SchemeCell.EMPTY_LIST));
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenNoArgs_whenAppend_thenShouldReturnEmptyList() {
        var program = "(append)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(SchemeCell.EMPTY_LIST, result);
    }

    @Test
    public void givenArbitraryNumberOfArgs_whenAppend_thenShouldReturnEmptyList() {
        var program = "(append (list 1 2) (list 3 4) (list 5 6))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult =
                new SchemeCell(1L,
                        new SchemeCell(2L,
                                new SchemeCell(3L,
                                        new SchemeCell(4L,
                                                new SchemeCell(5L,
                                                        new SchemeCell(6L, SchemeCell.EMPTY_LIST))))));
        assertEquals(expectedResult, result);
    }

    @Test(expected = SchemeException.class)
    public void givenOneListOnePair_whenAppend_thenShouldThrowException() {
        var program = "(append (list 1 2) (cons 3 4))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }
}
