package com.ihorak.truffle.builtin;

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
        var program = "(define-macro macro (lambda (test first) `(if ,test ,first #f))) (test #t 5)";

        var result = context.eval("scm", program);

        System.out.println(result);
    }


}
