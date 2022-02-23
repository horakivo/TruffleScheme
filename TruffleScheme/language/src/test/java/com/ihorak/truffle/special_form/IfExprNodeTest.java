package com.ihorak.truffle.special_form;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IfExprNodeTest {

    @Test
    public void givenNumberInTestClause_whenIfExecuted_thenCorrectResultReturned() {
        var program = "(if 11 88)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(88L, result);
    }

    @Test
    public void givenTrueInTestClause_whenIfExecuted_thenCorrectResultReturned() {
        var program = "(if #t (+ 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(3L, result);
    }

    @Test
    public void givenFalseInTestClause_whenIfExecuted_thenCorrectResultReturned() {
        var program = "(if #f (+ 1 2) (- 2 1))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(1L, result);
    }

    @Test
    public void givenFalseInTestClauseAndNoElseBranch_whenIfExecuted_thenNullShouldBeReturned() {
        var program = "(if #f (+ 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        //TODO represent void correctly
        assertNull(result);
    }
}
