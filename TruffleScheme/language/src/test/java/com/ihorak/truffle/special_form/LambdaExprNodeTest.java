package com.ihorak.truffle.special_form;

import com.ihorak.truffle.runtime.UndefinedValue;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LambdaExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenLambdaExpr_whenExecuted_thenShouldReturnSchemeFunction() {
        var program = "(lambda (x) (define y 12) (+ x 2) (+ x 3) #f)";

        var result = context.eval("scm", program);

        assertTrue(result.canExecute());
        var procedureResult = result.execute(5);
        assertTrue(procedureResult.isBoolean());
        assertFalse(procedureResult.asBoolean());
    }


    @Test
    public void givenNestedLambdaWithTCO_whenExecuted_thenTCOCatcherIsCreated() {
        var program = """
                (define test
                  (lambda (len)
                    (lambda (x)
                      (define generate
                        (lambda (q w e r y)
                          (let ((xxx 100))
                            (list 1 2 generate))))
                      (generate len 101 17 3 '()))))
                      
                ((test 5) 5)
                                
                """;

        var result = context.eval("scm", program);

        assertEquals("(1 2 #<user_procedure>:generate>)", result.toString());
    }

    @Test
    public void givenLambdaExpr_whenApplied_thenShouldCorrectResult() {
        var program = "((lambda (x) x) 5)";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenLambdaExpr_whenApplied_thenAllBuiltinProcedureShouldBeConverted() {
        var program = "((lambda (x) x) (+ (+ 1 2) 5))";

        var result = context.eval("scm", program);

        assertEquals(8L, result.asLong());
    }

    @Test
    public void givenAddLambdaExpr_whenApplied_thenAllArgumentsShouldBeAdded() {
        var program = "((lambda (x y z) (+ x y z)) 1 2 3)";

        var result = context.eval("scm", program);

        assertEquals(6L, result.asLong());
    }

    @Test
    public void givenMinusLambdaExpr_whenApplied_thenAllArgumentsShouldBeSubtracted() {
        var program = "((lambda (x y z) (- x y z)) 1 2 3)";

        var result = context.eval("scm", program);

        assertEquals(-4L, result.asLong());
    }

    @Test
    public void givenMinusLambdaExprWithOneArg_whenApplied_thenNumberShouldBeNegated() {
        var program = "((lambda (x) (- x)) 4)";

        var result = context.eval("scm", program);

        assertEquals(-4L, result.asLong());
    }

    @Test
    public void givenComplexLambdaExpr_whenApplied_thenCorrectResultShouldBeReturned() {
        var program = "((lambda (x) (+ ((lambda () (+ 1 2))) x)) 5)";

        var result = context.eval("scm", program);

        assertEquals(8L, result.asLong());
    }

    @Test
    public void givenNestedLambda_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) ((lambda (y) (+ x y)) 5)) 10)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void givenDefinedNestedLambda_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(define fun (lambda (x) ((lambda (y) (+ x y)) 1))) (fun 5) (fun 20)";

        var result = context.eval("scm", program);

        assertEquals(21L, result.asLong());
    }

    @Test
    public void givenLambdaWithDefine_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) (define y 10) (+ x y)) 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void givenLambdaWithRedefinitionOfLocalVariable_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) (define x 10) x) 5)";

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

    @Test
    public void givenNestedLambdaWithDefine_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) ((lambda (y) (define y 10) (+ x y)) 20)) 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void giveLambdaWithDefineOutside_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(define fun (lambda (x) (+ x y))) (define y 10) (fun 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void lexicalScopeIsResolvedCorrectlyWhenGlobalVariableIsNotDefined() {
        var program = "(define foo (lambda () x)) (define bar (lambda (x) (foo))) (bar 1)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("'x: undefined\n" +
                "cannot reference an identifier before its definition", msg);
    }

    @Test
    public void lexicalScopeIsResolvedCorrectlyWhenGlobalVariableIsDefined() {
        var program = "(define foo (lambda () x)) (define bar (lambda (x) (foo))) (define x 88) (bar 1)";

        var result = context.eval("scm", program);

        assertEquals(88L, result.asLong());
    }

    @Test
    public void givenUserProcedure_whenCalledWithWrongNumberOfArgs_thenThrowException() {
        var program = "(define foo (lambda (a b) (+ a b))) (foo 1 2 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                foo: arity mismatch; Expected number of arguments does not match the given number
                expected: 2
                given: 3""", msg);
    }

    @Test
    public void givenUserProcedureInTCO_whenCalledWithWrongNumberOfArgs_thenThrowException() {
        var program = """
                (define foo (lambda (a b) (+ a b)))
                (define baz (lambda (n) (foo 1 2 3)))
                                
                (baz 10)
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                foo: arity mismatch; Expected number of arguments does not match the given number
                expected: 2
                given: 3""", msg);
    }
}
