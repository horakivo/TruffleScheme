package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
