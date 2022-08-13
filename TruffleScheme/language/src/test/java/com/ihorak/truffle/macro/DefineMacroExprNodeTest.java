package com.ihorak.truffle.macro;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefineMacroExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }



    @Test
    public void givenDefineMacro_whenEvaluated_thenSchemeMacroShouldBeReturned() {
        var program = "(define-macro macro (lambda (test first) `(if ,test ,first #f)))  (macro #f 5)";

        var result = context.eval("scm", program);

        System.out.println(result);
    }


    /**
     * APPROACH 1
     * ------------------------------
     *
     * (fib 10000)
     *
     * (defmacro test (condition then)
     *   `(if ,(fun condition))
     *        ,then
     *      nil))
     *  (test 1 2)

     *
     *  converter - (if metadata1 metadata2 nil) - this needs to be some kind of truffle AST tree (not finished)
     *  when called e.g (test 1 2)
     *  evaluator
     *      1. takes both arguments (as a SchemeExpression, therefore it is an AST) and add it to the AST above
     *  problem: AST could never specialize since we would every call change its kids
     *
     *
     *  APPROACH 2
     *  ------------------------------
     * (defmacro test (condition then)
     *   `(if ,condition
     *        ,then
     *      nil))
     *
     * (test 1 2)
     *
     *  Overall idea: some kind of callable thingy
     *  BIG PROBLEM: I will then evaluate all the args which is conceptionally just wrong
     *
     *  converter - (if readFirstArg readSecondArg nil)
     *  evaluator - no changes, just eval this
     *
     *
     *
     * (defun fun1 ()
     *   (let ((x 10))
     *     (defmacro macro1 ()
     *       `(if ,x
     *            "true"
     *          "false"))
     *     (macro1)))
     *
     *
     * (defun fun2 ()
     *   (let ((y 10))
     *     (defmacro macro2 ()
     *       `(if ,x
     *            "true"
     *          "false"))
     *     (macro2)))
     *
     *
     *
     * */

    @Test
    public void givenDefineMacroWithUnknownValue_whenEvaluated_thenSchemeMacroShouldBeReturned() {
        var program = "(define-macro macro (lambda (test first) (list 'if ivo first #f))) (macro #f 5)";

        var result = context.eval("scm", program);

        System.out.println(result);
    }

}
