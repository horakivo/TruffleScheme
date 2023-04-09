package com.ihorak.truffle.macro;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DefineMacroExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenDefineMacro_whenEvaluated_thenUndefinedShouldBeReturned() {
        var program = "(define-macro macro (lambda (test first) `(if ,test ,first #f)))";

        var result = context.eval("scm", program);

        assertTrue(result.isNull());
        assertEquals("undefined", result.toString());
    }

    @Test
    public void givenDefineMacro_whenCalled_thenCorrectResultShouldBeReturned() {
        var program = "(define-macro new-if (lambda (condition then) `(if ,condition ,then #f))) (new-if (= 10 5) 5)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenMacro_whenCalledMultipleTimesInFunction_thenMacroTreeIsExpandedCorrectlyAndCorrectResultIsReturned() {
        var program = """
                        (define-macro new-if
                          (lambda (condition then)
                            `(if ,condition
                                 ,then
                                 #f)))
                                 
                        (define call-macro
                          (lambda (condition then)
                            (new-if condition then)))
                        
                        (call-macro #f 5)
                        (call-macro #t 11)
                        (call-macro (= 5 5) 10)
                        """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

    @Test
    public void givenMacroWithWrongNumberOfArguments_whenCalled_thenExceptionIsRaised() {
        var program = "(define-macro new-if (lambda (condition then) `(if ,condition ,then #f))) (new-if (= 10 5) 5 #f)";

        var result = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                macro 'new-if was called with wrong number of arguments
                expected: 2
                given: 3
                """, result);
    }

    @Test
    public void givenDefineMacroWithWrongNumberOfArgs_whenCalled_thenExceptionIsRaised() {
        var program = "(define-macro (lambda (condition then) `(if ,condition ,then #f))) (new-if (= 10 5) 5 #f)";

        var result = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                define-macro: arity mismatch; Expected number of arguments does not match the given number
                expected: 3
                given: 2""", result);
    }

    @Test
    public void givenDefineMacroWhereDefinitionIsNotAllowed_whenCalled_thenExceptionIsRaised() {
        var program = "(define test (define-macro (lambda (condition then) `(if ,condition ,then #f))))";

        var result = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                define-macro: not allowed in an expression context""", result);
    }


    @Test
    public void givenDefineMacroWithWrongIdentifierType_whenCalled_thenExceptionIsRaised() {
        var program = "(define-macro \"macro-name\" (lambda (condition then) `(if ,condition ,then #f))) (new-if (= 10 5) 5 #f)";

        var result = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                define-macro: contract violation
                expected: symbol?
                given: macro-name""", result);
    }


    @Test
    public void givenDefineMacroWithUnknownValue_whenEvaluated_thenSchemeMacroShouldBeReturned() {
        var program = "(define-macro macro (lambda (test first) (list 'if ivo first #f))) (macro #f 5)";

        var result = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                'ivo: undefined
                cannot reference an identifier before its definition""", result);
    }
}
