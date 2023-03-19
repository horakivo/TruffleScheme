package com.ihorak.truffle.special_form;

import com.ihorak.truffle.type.UndefinedValue;
import org.graalvm.polyglot.Context;
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
        //TODO what to pass here as a first argument?
        var procedureResult = result.execute(UndefinedValue.SINGLETON, 5L);
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

        assertEquals("(1 2 #<user_procedure>)", result.toString());
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

//    @Test
//    public void givenSimpleLambdaWithUndefinedVariable_whenExecuted_thenCorrectResultIsReturned() {
//        var program = "(define fun (lambda () (+ x))) (fun)";
//        var rootNode = Reader.readProgram(CharStreams.fromString(program));
//
//        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();
//
//        assertEquals(21L, result);
//    }

//    @Test
//    public void fast_test_2() {
//        var program = "((lambda (x) (define plus +) (plus x 5)) 5)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(10L, result);
//    }
//
//    @Test
//    public void fast_test_3() {
//        var program = "((lambda (x) (define plus +) (+ 1 2) (plus x 5)) 5)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(10L, result);
//    }


    @Test
    public void givenLambdaWithEvalAndQuote_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) (eval '(define y 10)) (+ x y)) 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
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
    public void giveLambdaWithOptionalNumberOfArgs_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x y . rest) (list x y rest)) 1 2)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertTrue(result.getArrayElement(2).hasArrayElements());
        assertEquals(0L, result.getArrayElement(2).getArraySize());
    }

    @Test
    public void giveLambdaWithOptionalNumberOfArgs2_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x y . rest) (list x y rest)) 1 2 3)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertTrue(result.getArrayElement(2).hasArrayElements());
        assertEquals(1L, result.getArrayElement(2).getArraySize());
        assertEquals(3L, result.getArrayElement(2).getArrayElement(0).asLong());
    }

    @Test
    public void giveLambdaWithOptionalNumberOfArgs3_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x y . rest) (list x y rest)) 1 2 3 4)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertTrue(result.getArrayElement(2).hasArrayElements());
        assertEquals(2L, result.getArrayElement(2).getArraySize());
        assertEquals(3L, result.getArrayElement(2).getArrayElement(0).asLong());
        assertEquals(4L, result.getArrayElement(2).getArrayElement(1).asLong());
    }
}
