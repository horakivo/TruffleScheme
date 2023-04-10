package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuasiquoteExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenNumberAsBacktick_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "`5";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenIfExpr_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quasiquote (if (+ 3 4) 5 4))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("if", result.getArrayElement(0).asString());
        assertEquals("+", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals(3L, result.getArrayElement(1).getArrayElement(1).asLong());
        assertEquals(4L, result.getArrayElement(1).getArrayElement(2).asLong());
        assertEquals(5L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenLambdaExpr_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "(quasiquote (lambda (x y) (+ 1 2) #f))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("lambda", result.getArrayElement(0).asString());
        assertEquals("x", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals("y", result.getArrayElement(1).getArrayElement(1).asString());
        assertEquals("+", result.getArrayElement(2).getArrayElement(0).asString());
        assertEquals(1L, result.getArrayElement(2).getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).getArrayElement(2).asLong());
        assertFalse(result.getArrayElement(3).asBoolean());
    }

    @Test
    public void givenIfExprAsBacktick_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "`(if (+ 3 4) 5 4)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("if", result.getArrayElement(0).asString());
        assertEquals("+", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals(3L, result.getArrayElement(1).getArrayElement(1).asLong());
        assertEquals(4L, result.getArrayElement(1).getArrayElement(2).asLong());
        assertEquals(5L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenLambdaExprAsBacktick_whenQuasiquoteIsExecuted_thenCorrectListShouldBeReturned() {
        var program = "`(lambda (x y) (+ 1 2) #f)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("lambda", result.getArrayElement(0).asString());
        assertEquals("x", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals("y", result.getArrayElement(1).getArrayElement(1).asString());
        assertEquals("+", result.getArrayElement(2).getArrayElement(0).asString());
        assertEquals(1L, result.getArrayElement(2).getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).getArrayElement(2).asLong());
        assertFalse(result.getArrayElement(3).asBoolean());
    }

    @Test
    public void givenLambdaWithQuasiquote_whenExexuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) `(if ,x #t #f)) 5)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("if", result.getArrayElement(0).asString());
        assertEquals(5L, result.getArrayElement(1).asLong());
        assertTrue(result.getArrayElement(2).asBoolean());
        assertFalse(result.getArrayElement(3).asBoolean());
    }

    @Test
    public void givenWrongNumberOfArgs_whenQuasiquoteIsExecuted_thenThrowParserException() {
        var program = "(quasiquote abc x)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("quasiquote: arity mismatch\n" +
                "expected: 1\n" +
                "given: 2", msg);
    }

    @Test
    public void givenDefineToBeEval_whenExecuted_thenShouldThrowException() {
        var program = "`(list 1 ,(define x 2) ,@(list (+ 1 2) 4) 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("define: not allowed in an expression context", msg);
    }

    @Test
    public void givenUnquoteAsSyntacticSugar_whenExecuted_thenUnquoteIsCorrectlyEvaluated() {
        var program = "`(list 1 ,(+ 1 1) 3)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(4L, result.getArraySize());
        assertEquals("list", result.getArrayElement(0).asString());
        assertEquals(1L, result.getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).asLong());
        assertEquals(3L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenUnquote_whenExecuted_thenUnquoteIsCorrectlyEvaluated() {
        var program = "`(list 1 (unquote (+ 1 1)) 3)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(4L, result.getArraySize());
        assertEquals("list", result.getArrayElement(0).asString());
        assertEquals(1L, result.getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).asLong());
        assertEquals(3L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenNestedUnquoting_whenExecuted_thenShouldThrowSchemeException() {
        var program = "`(list 1 ,(list 1 2) ,@(list ,(+ 1 2) 4) 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("unquote: expression not valid outside of quasiquote in form ('unquote ('+ 1 2))", msg);
    }

    //TODO interesting that quile evals (display `(list 1 (unquote (list 1 2) 2) 3)) to (list 1 (1 2) 2 3)
    @Test
    public void givenMoreArgsToUnquote_whenExecuted_thenShouldThrowSchemeException() {
        var program = "`(list 1 (unquote (list 1 2) 2) 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("unquote: expects exactly one expression in ('unquote ('list 1 2) 2)", msg);
    }

    @Test
    public void givenMoreArgsToUnquoteSplicing_whenExecuted_thenShouldThrowSchemeException() {
        var program = "`(list 1 (unquote-splicing (list 1 2) 2) 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("unquote-splicing: expects exactly one expression in ('unquote-splicing ('list 1 2) 2)", msg);
    }


    @Test
    public void givenSimpleNestedUnquote_whenExecuted_thenShouldThrowException() {
        var program = "`(list 1 (unquote (list ,1 2)) 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("unquote: expression not valid outside of quasiquote in form ('unquote 1)", msg);
    }

    @Test
    public void givenSimpleNestedUnquoteSplicing_whenExecuted_thenShouldThrowException() {
        var program = "`(list 1 (unquote (list ,@1 2)) 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("unquote-splicing: expression not valid outside of quasiquote in form ('unquote-splicing 1)", msg);
    }

    @Test
    public void givenNestedQuasiquote_whenExecuted_thenCorrectResultIsReturned() {
        var program = "`(1 `,,@(list (+ 1 2)) 4)";

        var result = context.eval("scm", program);

        assertEquals("(1 ('quasiquote ('unquote 3)) 4)", result.toString());
        assertTrue(result.hasArrayElements());
        assertEquals(3L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals("quasiquote", result.getArrayElement(1).getArrayElement(0).asString());
        assertEquals("unquote", result.getArrayElement(1).getArrayElement(1).getArrayElement(0).asString());
        assertEquals(3L, result.getArrayElement(1).getArrayElement(1).getArrayElement(1).asLong());
        assertEquals(4L, result.getArrayElement(2).asLong());
    }

    @Test
    public void givenNestedQuasiquoteWithMoreUnquoteDepth_whenExecuted_thenExceptionIsThrown() {
        var program = "`(1 `,,,(list (+ 1 2)) 4)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("unquote: expression not valid outside of quasiquote in form ('unquote ('list ('+ 1 2)))", msg);
    }

    @Test
    public void givenUnquoteSplicingAsAFirstElement_whenExecuted_thenUnquoteSplicingIsNotActioned() {
        var program = "`,@(list 1 2)";

        var result = context.eval("scm", program);

        assertEquals("('unquote-splicing ('list 1 2))", result.toString());
    }

    @Test
    public void givenUnquoteSplicingAsFirstElementInList_whenExecuted_thenCorrectResultIsReturned() {
        var program = "`(,@(list 1 2) 3)";

        var result = context.eval("scm", program);

        assertEquals("(1 2 3)", result.toString());
    }

    @Test
    public void quasiquoteDoesntModifyGivenData() {
        var program = """
                (define data '`(if ,(+ 1 1) 1 2))
                (define-macro macro
                  (lambda () data))
                    
                (macro)
                data
                """;

        var result = context.eval("scm", program);

        assertEquals("('quasiquote ('if ('unquote ('+ 1 1)) 1 2))", result.toString());
    }
}
