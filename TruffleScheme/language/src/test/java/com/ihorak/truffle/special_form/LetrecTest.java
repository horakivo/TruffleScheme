package com.ihorak.truffle.special_form;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LetrecTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSimpleLetrec_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(letrec\n" +
                "    ((x 10)\n" +
                "     (y x))\n" +
                "  (+ x y))";

        var result = context.eval("scm", program);

        assertEquals(20L, result.asLong());
    }

    @Test
    public void givenSimpleLetrecFromDocs_whenExecuted_thenCorrectResultIsReturned() {
        var program = "" +
                "(letrec ((is-even? (lambda (n)\n" +
                "                     (or (= n 0)\n" +
                "                         (is-odd? (- n 1)))))\n" +
                "         (is-odd? (lambda (n)\n" +
                "                    (and (not (= n 0))\n" +
                "                         (is-even? (- n 1))))))\n" +
                "  (is-odd? 11))";

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void aaa() {
        var program = """
                (define test
                  (lambda ()
                    (letrec ((is-even? (lambda (n)
                                       (or (= 0 n)
                                           (is-odd? (- n 1)))))
                           (is-odd? (lambda (n)
                                      (and (not (= 0 n))
                                           (is-even? (- n 1))))))
                    (is-odd? 11))))
                    
                (test)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenLetrecWithUndefinedValue_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "" +
                "(letrec\n" +
                "    ((x y)\n" +
                "     (y x))\n" +
                "  (+ x y))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                             'y: undefined
                             cannot reference an identifier before its definition""", msg);
    }

    @Test
    public void givenNestedLetrec_whenExecuted_thenExceptionShouldBeThrown() {
        var program = """
                (letrec
                    ((x 10)
                     (y (letrec ((z x)) z)))
                  (+ x y))
                """;

        var result = context.eval("scm", program);

        assertEquals(20L, result.asLong());
    }
}
