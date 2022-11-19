package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LetExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSimpleLet_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(let ((x 10) (y 15)) (+ x y) (- x y))";

        var result = context.eval("scm", program);

        assertEquals(-5L, result.asLong());
    }

    @Test
    public void givenNestedLets_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(let ((x 10)) (let ((y (* x x))) (let ((z (- y x))) (+ z y))))";


        var result = context.eval("scm", program);

        assertEquals(190L, result.asLong());
    }

    @Test
    public void givenValueFromGlobalEnv_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(define x 100)\n" +
                "(let ((x 10)\n" +
                "      (y (* 2 x)))\n" +
                "  (+ x y))";


        var result = context.eval("scm", program);

        assertEquals(210L, result.asLong());
    }

    @Test
    public void givenRedefinedValue_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(let ((x 10))\n" +
                "  (define x 100)\n" +
                "  (+ x 10))";


        var result = context.eval("scm", program);

        assertEquals(110L, result.asLong());
    }


    @Test
    public void givenLetWithGlobalReferenceInBinding_whenExecuted_correctResultIsReturned() {
        var program = "" +
                "(define fun\n" +
                "  (lambda ()\n" +
                "    (let ((x 10)\n" +
                "          (y (+ x 10)))\n" +
                "      5)))\n" +
                "\n" +
                "(define x 10)\n" +
                "(fun)";


        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void givenMacroDefinedInLambda_whenExecuted_thenLexicalScopeIsStillRight() {
        var program = "" +
                "(define fun\n" +
                "  (lambda (var)\n" +
                "    (define-macro test\n" +
                "      (lambda (test)\n" +
                "        `(if ,test\n" +
                "             #t\n" +
                "             #f)))\n" +
                "    (let ((x 10)\n" +
                "          (y (+ x 10)))\n" +
                "      (+ var x y (test 10)))))\n" +
                "\n" +
                "(define x 10)\n" +
                "(fun 10)";


        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                             +: contract violation;
                             expected: number?
                             given: [20,true]""", msg);
    }

    @Test
    public void givenLetWithUnknownVariableInBinding_whenExecuted_exceptionIsThrown() {
        var program = "" +
                "(define fun\n" +
                "  (lambda ()\n" +
                "    (let ((x 10)\n" +
                "          (y (+ x 10)))\n" +
                "      5)))\n" +
                "\n" +
                "(fun)\n" +
                "(define x 10)";


        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                             'x: undefined
                             cannot reference an identifier before its definition""", msg);
    }

    @Test
    public void givenLetInSideLambda_whenExecuted_thenLexicalScopeIsStillRight() {
        var program = "" +
                "(define fun\n" +
                "  (lambda (var)\n" +
                "    (let ((x 10)\n" +
                "          (y (+ x 10)))\n" +
                "      (+ var x y))))\n" +
                "\n" +
                "(define x 10)\n" +
                "(fun 10)\n";


        var result = context.eval("scm", program);

        assertEquals(40L, result.asLong());
    }

    @Test
    public void givenLambdaAndLetWithSameParameter_whenExecuted_thenLexicalScopeIsResolvedCorrectly() {
        var program = "" +
                "(define fun\n" +
                "  (lambda (y)\n" +
                "    (define y 30)\n" +
                "    (let ((y 20))\n" +
                "      100)\n" +
                "    (+ y 200)))\n" +
                "\n" +
                "(fun 10)";


        var result = context.eval("scm", program);

        assertEquals(230L, result.asLong());
    }

    @Test
    public void givenWrongSyntax_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(let ((x 10 5) (y 15)) (+ x y) (- x y))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                             let: bad syntax
                             expected size of binding is 2
                             given: 3""", msg);
    }
}
