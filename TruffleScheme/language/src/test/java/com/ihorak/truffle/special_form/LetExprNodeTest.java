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
    public void givenWrongSyntax_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(let ((x 10 5) (y 15)) (+ x y) (- x y))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("let: bad syntax (not an identifier and expression for a binding)", msg);
    }
}
