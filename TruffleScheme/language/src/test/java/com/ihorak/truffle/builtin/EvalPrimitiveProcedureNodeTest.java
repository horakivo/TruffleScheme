package com.ihorak.truffle.builtin;

import com.ihorak.truffle.convertor.util.BuiltinUtils;
import com.ihorak.truffle.type.UndefinedValue;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class EvalPrimitiveProcedureNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @BeforeClass
    public static void before() {
        BuiltinUtils.isBuiltinEnabled = false;
    }

    @Test
    public void givenRandomNumber_whenEvaluated_thenReturnCorrectResult() {
        var program = "(eval 5)";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenPlusExpr_whenEvaluated_thenReturnCorrectResult() {
        var program = "(eval (+ 1 2 3))";

        var result = context.eval("scm", program);

        assertEquals(6L, result.asLong());
    }

    @Test
    public void givenEvalWithWrongNumberOfArguments_whenExecuted_EvalShouldThrowException() {
        var program = "(eval 1 2)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("eval: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 1\n" +
                "given: 2", msg);
    }

    @Test
    public void givenLambdaExpr_whenEvaluated_thenSchemeFunctionShouldBeReturned() {
        var program = "(eval (lambda () (+ 1 2)))";

        var result = context.eval("scm", program);

        assertTrue(result.canExecute());
        assertEquals(3L, result.execute(UndefinedValue.SINGLETON).asLong());
    }

    @Test
    public void givenQuoteExprSymbol_whenEvaluated_thenListShouldBeEvaluatedCorrectly() {
        var program = "(eval 'a)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("'a: undefined\n" +
                "cannot reference an identifier before its definition", msg);
    }

    @Test
    public void givenQuoteExprList_whenEvaluated_thenListShouldBeEvaluatedCorrectly() {
        var program = "(eval '(+ 1 2))";

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void givenPair_whenEvaluated_thenExceptionShouldBeThrown() {
        var program = "(eval '(1 . 2))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("eval: cannot evaluate pair", msg);
    }

    @Test
    public void givenEvalExprInLambda_whenExecuted_thenDefinedValueIsStillFound() {
        var program = "((lambda () (eval '(define x 22)) x))";

        var result = context.eval("scm", program);

        assertEquals(22L, result.asLong());
    }


    @Test
    public void givenLambdaExpr_whenExecuted_thenEvalShouldBeFoundInGlobalEnvAndExecutedCorrectly() {
        var program = "((lambda () (define abc eval) (abc 8)))";

        var result = context.eval("scm", program);

        assertEquals(8L, result.asLong());
    }

    @Test
    public void givenLambdaExprWithWrongNumberOfArguments_whenExecuted_EvalShouldThrowException() {
        var program = "((lambda () (define abc eval) (abc 8 5)))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("eval: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 1\n" +
                "given: 2", msg);
    }

    @AfterClass
    public static void after() {
        BuiltinUtils.isBuiltinEnabled = true;
    }
}
