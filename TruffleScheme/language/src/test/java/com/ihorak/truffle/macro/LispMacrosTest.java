package com.ihorak.truffle.macro;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

public class LispMacrosTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenDefineMacro_whenEvaluated_thenSchemeMacroShouldBeReturned() {
        var program = "(defmacro macro (lambda (test first) `(if ,test ,first #f))) (macro #f 5)";

        var result = context.eval("scm", program);

        System.out.println(result);
    }
}
