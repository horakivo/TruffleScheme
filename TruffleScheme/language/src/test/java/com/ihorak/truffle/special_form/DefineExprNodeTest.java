package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefineExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenAnyValue_whenDefineIsExecuted_thenCorrectValueShouldBeStored() {
        var program = "(define x 5) x";

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenDefinedValueOutsideFunction_whenExecuted_thenValueShouldBeFound() {
        var program = "(define foo (lambda (x) (+ x y))) (define y 10) (foo 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void givenRedefinedGlobalVariable_whenExecuted_thenValueShouldBeFound() {
        var program = "(define foo (lambda (x) (+ x y))) (define y 100) (foo 5) (define y 10) (foo 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void givenRecursiveDefineInLambdaWithUndefinedValue_whenExecuted_thenErrorShouldBeThrown() {
        var program = """
                (define random-list3
                  (lambda ()
                    (define x (list 1 2 x))
                    x))
                    
                (random-list3)
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                'x: undefined
                cannot reference an identifier before its definition""", msg);

    }

    /*
     * Idea of this test is that variable called generate should be as NON null since the execution is delayed
     * */
    @Test
    public void givenNestedDefineWithDelayedExecution_whenExecuted_thenCorrectResultIsReturned() {
        var program = """
                (define random-list4
                  (lambda (len)
                    (let ((xx 10))
                      (define generate
                        (lambda (q w e r y)
                          (let ((xxx 100))
                           (list 1 2 generate))))
                        
                      (generate len 101 17 3 '()))))
                (random-list4 10)""";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        //TODO should work when I implemented Lib
        //assertTrue(result.getArrayElement(1).canExecute());
    }


    /*
     * Here the execution is not delayed that's why exception is thrown
     * */
    @Test
    public void givenNestedDefineWithoutDelayed_whenExecuted_thenExceptionIsThrown() {
        var program = """
                (define random-list4
                  (lambda (len)
                    (let ((xx 10))
                      (define generate
                          (let ((xxx 100))
                           (list 1 2 generate)))

                      (generate len 101 17 3 '()))))
                (random-list4 10)""";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                'generate: undefined
                cannot reference an identifier before its definition""", msg);
    }


    /*
     * Here the value <test11> is found since it is on GLOBAL env and therefore we use ReadGlobalVariable
     * */
    @Test
    public void givenDefiningValueInsideLambdaBody_whenExecuted_thenValueIsFound() {
        var program = """
                (define test11
                  (lambda ()
                    (list 1 2 test11)))
                    
                (test11)""";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        //TODO should work when I implemented Lib
        //assertTrue(result.getArrayElement(1).canExecute());
    }
}
