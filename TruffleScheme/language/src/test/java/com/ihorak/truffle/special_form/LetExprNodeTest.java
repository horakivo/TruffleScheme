package com.ihorak.truffle.special_form;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LetExprNodeTest {

    @Test
    public void givenSimpleLet_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(let ((x 10) (y 15)) (+ x y) (- x y))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(-5L, result);
    }

    @Test
    public void givenNestedLets_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(let ((x 10)) (let ((y (* x x))) (let ((z (- y x))) (+ z y))))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(190L, result);
    }
}
