package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

public class MapExprNodeTest {

//    @Test
//    public void giveListAndMinus_whenMap_thenShouldListWithNegativeNumbers() {
//        var program = "(map - (list 1 2 3))";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        var expectedResult = new SchemeCell(-1L, new SchemeCell(-2L, new SchemeCell(-3L, SchemeCell.EMPTY_LIST)));
//
//        assertEquals(expectedResult, result);
//    }

//    @Test
//    public void giveListsAndPlus_whenMap_thenShouldReturnAddedAllLists() {
//        var program = "(map + (list 1 2 3) (list 4 5 6) (list 7 8 9))";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        var expectedResult = new SchemeCell(12L, new SchemeCell(15L, new SchemeCell(18L, SchemeCell.EMPTY_LIST)));
//
//        assertEquals(expectedResult, result);
//    }

//    //TODO potentionally move to JUNIT 5 since its not nice to check error messages here
//    @Test(expected = SchemeException.class)
//    public void giveListsWithDifferentNumberOfArgs_whenMap_thenShouldThrowException() {
//        var program = "(map + (list 1 2) (list 4 5 6) (list 7 8 9))";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//    }

    @Test(expected = SchemeException.class)
    public void givenProcedureWithDifferentNumberOfArgs_whenMap_thenShouldThrowException() {
        var program = "(map (lambda (x) (- x)) (list 1 2 3) (list 4 5 6) (list 7 8 9))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }
}
