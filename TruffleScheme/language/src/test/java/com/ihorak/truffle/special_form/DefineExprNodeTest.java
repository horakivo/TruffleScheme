package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
