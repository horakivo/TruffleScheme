package com.ihorak.truffle.builtin;

import com.ihorak.truffle.type.SchemeList;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProgramTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void test1() {
        var program = "(define fib (lambda (n) (if (<= n 2) 1 (+ (fib (- n 1)) (fib (- n 2)))))) (fib 30)";

        var result = context.eval("scm", program);

        assertEquals(832040L, result.asLong());
    }

    @Test
    public void test2() {
        var program = "(define foo (lambda () x)) (define bar (lambda (x) (foo))) (bar 1)";

        var exceptionMsg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("'x: undefined\n" +
                             "cannot reference an identifier before its definition", exceptionMsg);
    }

    @Test
    public void test3() {
        var program = "(define foo (lambda () x)) (define bar (lambda (x) (foo))) (define x 88) (bar 1)";

        var result = context.eval("scm", program);

        assertEquals(88L, result.asLong());
    }



    @Test
    public void test9() {
        var program = "" +
                "(define foo\n" +
                "  (lambda (x)\n" +
                "    (a x)))\n" +
                "\n" +
                "(define a\n" +
                "  (lambda (y)\n" +
                "    (b y)))\n" +
                "\n" +
                "(define b\n" +
                "  (lambda (z)\n" +
                "    (c z)))\n" +
                "\n" +
                "(define c\n" +
                "  (lambda (q)\n" +
                "    q))\n" +
                "\n" +
                "(foo 5)";

        var result = context.eval("scm", program);
        System.out.println(result);
    }
}
