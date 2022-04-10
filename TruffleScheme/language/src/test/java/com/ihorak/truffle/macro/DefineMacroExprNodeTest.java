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

    @Test
    public void givenDefineMacroWithUnknownValue_whenEvaluated_thenSchemeMacroShouldBeReturned() {
        var program = "(define-macro macro (lambda (test first) (list 'if ivo first #f))) (macro #f 5)";

        var result = context.eval("scm", program);

        System.out.println(result);
    }

}
