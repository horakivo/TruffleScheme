package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsExprNodeTest {

    @Test
    public void givenRandomNumbers_whenCons_thenReturnSchemeCell() {
        var program = "(cons 1 2)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(1L, 2L);

        assertEquals(expectedResult, result);
        assertEquals("(1 . 2)", result.toString());
    }

    @Test
    public void givenComplexStructure_whenCons_thenReturnCorrectValue() {
        var program = "(cons (cons 1 2) (cons 3 4))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(new SchemeCell(1L, 2L), new SchemeCell(3L, 4L));

        assertEquals(expectedResult, result);
        assertEquals("((1 . 2) . (3 . 4))", result.toString());
    }

    @Test
    public void givenLastElementEmptyList_whenCons_thenResultList() {
        var program = "(cons (cons 1 2) (list))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(new SchemeCell(1L, 2L), SchemeCell.EMPTY_LIST);

        assertEquals(expectedResult, result);
        assertEquals("((1 . 2))", result.toString());
    }

    @Test
    public void givenLastElementList_whenCons_thenResultList() {
        var program = "(cons (cons 1 2) (list 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(new SchemeCell(1L, 2L), new SchemeCell(1L, new SchemeCell(2L, SchemeCell.EMPTY_LIST)));

        assertEquals(expectedResult, result);
        assertEquals("((1 . 2) 1 2)", result.toString());
    }
}
