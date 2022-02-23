package com.ihorak.truffle.special_form;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.ParserException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class QuoteExprNodeTest {

    @Test
    public void givenIfExpr_whenQuoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quote (if (+ 3 4) 5 4))";
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
    public void givenLambdaExpr_whenQuoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quote (lambda (x y) (+ 1 2) #f))";
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
    public void givenRandomList_whenQuoteAsTickIsExecuted_thenListAsDataShouldBeReturned() {
        var program = "'(1 2 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        var expectedResult = new SchemeCell(1L, new SchemeCell(2L, new SchemeCell(3L, SchemeCell.EMPTY_LIST)));

        assertEquals(expectedResult, result);
    }

    @Test
    public void givenLambdaExpr_whenQuoteAsTickIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "'(lambda (x y) (+ 1 2) #f)";
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
    public void givenRandomNumber_whenQuoteAsTickIsExecuted_thenInternalReprOfNumberShouldBeReturned() {
        var program = "'5";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());


        assertEquals(5L, result);
    }

    @Test
    public void givenRandomSymbol_whenQuoteAsTickIsExecuted_thenInternalReprOfNumberShouldBeReturned() {
        var program = "'abc";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());


        assertEquals(new SchemeSymbol("abc"), result);
    }

    @Test
    public void givenWrongNumberOfArgs_whenQuoteExecuted_thenThrowParserException() {
        var program = "(quote abc x)";
        var exception = assertThrows(ParserException.class, () -> Reader.readExpr(CharStreams.fromString(program)));

        assertEquals("quote: arity mismatch \n" +
                " expected: 1 \n" +
                " given: 2", exception.getMessage());
    }
}
