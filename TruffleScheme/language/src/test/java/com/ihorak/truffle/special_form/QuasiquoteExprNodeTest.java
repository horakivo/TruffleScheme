package com.ihorak.truffle.special_form;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class QuasiquoteExprNodeTest {

    @Test
    public void givenNumberAsBacktick_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "`5";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(5L, result);
    }

    @Test
    public void givenIfExpr_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quasiquote (if (+ 3 4) 5 4))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult =
                new SchemeCell(new SchemeSymbol("if"),
                        new SchemeCell(
                                new SchemeCell(new SchemeSymbol("+"),
                                        new SchemeCell(3L,
                                                new SchemeCell(4L, SchemeCell.EMPTY_LIST))),
                                new SchemeCell(5L, new SchemeCell(4L, SchemeCell.EMPTY_LIST))));


        assertEquals(expectedResult, result);
    }

    @Test
    public void givenLambdaExpr_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quasiquote (lambda (x y) (+ 1 2) #f))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult =
                new SchemeCell(new SchemeSymbol("lambda"),
                        new SchemeCell(
                                new SchemeCell(new SchemeSymbol("x"),
                                        new SchemeCell(
                                                new SchemeSymbol("y"), SchemeCell.EMPTY_LIST)),
                                new SchemeCell(
                                        new SchemeCell(new SchemeSymbol("+"),
                                                new SchemeCell(1L,
                                                        new SchemeCell(2L, SchemeCell.EMPTY_LIST))),
                                        new SchemeCell(false, SchemeCell.EMPTY_LIST))));


        assertEquals(expectedResult, result);
    }

    @Test
    public void givenIfExprAsBacktick_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "`(if (+ 3 4) 5 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult =
                new SchemeCell(new SchemeSymbol("if"),
                        new SchemeCell(
                                new SchemeCell(new SchemeSymbol("+"),
                                        new SchemeCell(3L,
                                                new SchemeCell(4L, SchemeCell.EMPTY_LIST))),
                                new SchemeCell(5L, new SchemeCell(4L, SchemeCell.EMPTY_LIST))));


        assertEquals(expectedResult, result);
    }

    @Test
    public void givenLambdaExprAsBacktick_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "`(lambda (x y) (+ 1 2) #f)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        var expectedResult =
                new SchemeCell(new SchemeSymbol("lambda"),
                        new SchemeCell(
                                new SchemeCell(new SchemeSymbol("x"),
                                        new SchemeCell(
                                                new SchemeSymbol("y"), SchemeCell.EMPTY_LIST)),
                                new SchemeCell(
                                        new SchemeCell(new SchemeSymbol("+"),
                                                new SchemeCell(1L,
                                                        new SchemeCell(2L, SchemeCell.EMPTY_LIST))),
                                        new SchemeCell(false, SchemeCell.EMPTY_LIST))));


        assertEquals(expectedResult, result);
    }

    @Test(expected = ParserException.class)
    public void givenWrongNumberOfArgs_whenQuasiquoteIsExecuted_thenThrowParserException() {
        var program = "(quasiquote abc x)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }

    @Test
    public void givenDefineToBeEval_whenExecuted_thenShouldThrowException() {
        var program = "`(list 1 ,(define x 2) ,@(list (+ 1 2) 4) 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var exception = assertThrows(SchemeException.class, () -> expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame()));

        assertEquals("define: not allowed in an expression context in: ('define 'x 2)", exception.getMessage());
    }

    @Test
    public void givenNestedUnquoting_whenExecuted_thenShouldThrowSchemeException() {
        var program = "`(list 1 ,(list 1 2) ,@(list ,(+ 1 2) 4) 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var exception = assertThrows(SchemeException.class, () -> expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame()));

        assertEquals("unquote or unquote-splicing: not in quasiquote in: ('unquote ('+ 1 2))", exception.getMessage());
    }

    @Test
    public void givenMoreArgsToUnquote_whenExecuted_thenShouldThrowSchemeException() {
        var program = "`(list 1 (unquote (list 1 2) 2) 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var exception = assertThrows(SchemeException.class, () -> expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame()));

        assertEquals("unquote: expects exactly one expression", exception.getMessage());
    }

    @Test
    public void givenSimpleNestedUnquote_whenExecuted_thenShouldThrowException() {
        var program = "`(list 1 (unquote (list ,1 2)) 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var exception = assertThrows(SchemeException.class, () -> expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame()));

        assertEquals("unquote or unquote-splicing: not in quasiquote in: ('unquote 1)", exception.getMessage());
    }
}
