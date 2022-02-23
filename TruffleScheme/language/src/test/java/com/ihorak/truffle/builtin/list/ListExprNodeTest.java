package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListExprNodeTest {

    @Test
    public void givenSomeArgs_whenListCreated_thenReturnCorrectSchemeCell() {
        var program = "(list 1 2 3 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(1L, new SchemeCell(2L, new SchemeCell(3L, new SchemeCell(4L, SchemeCell.EMPTY_LIST))));
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenNoArgs_whenEvaluated_thenReturnEmptyList() {
        var program = "(list)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(SchemeCell.class, result.getClass());
        assertEquals(SchemeCell.EMPTY_LIST, result);
    }
}
