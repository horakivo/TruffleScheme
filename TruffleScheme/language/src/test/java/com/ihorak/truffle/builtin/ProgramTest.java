package com.ihorak.truffle.builtin;

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
    public void test7() {
        var program = "" +
                "(define fact\n" +
                "  (lambda (n)\n" +
                "    (if (= n 0)\n" +
                "        1\n" +
                "        (* n (- n 1)))))\n" +
                "\n" +
                "(fact 5312)";

        var result = context.eval("scm", program);

        assertEquals(28212032L, result.asLong());
    }

//    @Test
//    public void test6() {
//        var program = "" +
//                "(define fact\n" +
//                "  (lambda (n)\n" +
//                "    (if (= n 0)\n" +
//                "        1\n" +
//                "        (* n (fact (- n 1))))))\n" +
//                "\n" +
//                "\n" +
//                "(fact 10000)\n" +
//                "(fact 10000)\n" +
//                "(fact 10000)\n" +
//                "(fact 10000)\n" +
//                "(fact 10000)\n" +
//                "(fact 10000)\n" +
//                "\n" +
//                "(define start (current-milliseconds))\n" +
//                "\n" +
//                "(fact 10000)\n" +
//                "\n" +
//                "(define end (current-milliseconds))\n" +
//                "\n" +
//                "(- end start)";
//        var result = context.eval("scm", program);
//        System.out.println(result);
//    }

    @Test
    public void test8() {
        var program = "" +
                "(define countdown\n" +
                "  (lambda (n)\n" +
                "    (if (< n 1)\n" +
                "        0\n" +
                "        (cons 1 (countdown (- n 1))))))\n" +
                "\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(countdown 100000)\n" +
                "\n" +
                "(define end (current-milliseconds))\n" +
                "(- end start)";

        var result  = context.eval("scm", program);

    }

    @Test
    public void tak() {
        var program = "" +
                "(define tak\n" +
                "  (lambda (x y z)\n" +
                "    (if (not (< y x))\n" +
                "        z\n" +
                "        (tak (tak (- x 1) y z)\n" +
                "           (tak (- y 1) z x)\n" +
                "           (tak (- z 1) x y)))))\n" +
                "\n" +
                "\n" +
                "(define loop\n" +
                "  (lambda (n)\n" +
                "    (tak 18 12 6)\n" +
                "    (if (> n 0) (loop (- n 1)))))\n" +
                "\n" +
                "\n" +
                "(loop 100)\n" +
                "(loop 100)\n" +
                "(loop 100)\n" +
                "(loop 100)\n" +
                "(loop 100)\n" +
                "(loop 100)\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(loop 100)\n" +
                "\n" +
                "(define end (current-milliseconds))\n" +
                "(- end start)";

        var result  = context.eval("scm", program);
        System.out.println(result);

    }

    @Test
    public void primes() {
        var program = "\n" +
                "(define interval-list\n" +
                "  (lambda (m n)\n" +
                "    (if (> m n)\n" +
                "        '()\n" +
                "        (cons m (interval-list (+ 1 m) n)))))\n" +
                "\n" +
                "(define sieve\n" +
                "  (lambda  (l)\n" +
                "    (letrec ((remove-multiples\n" +
                "              (lambda (n l)\n" +
                "                (if (null? l)\n" +
                "                    '()\n" +
                "                    (if (= (modulo (car l) n) 0)\n" +
                "                        (remove-multiples n (cdr l))\n" +
                "                        (cons (car l)\n" +
                "                              (remove-multiples n (cdr l))))))))\n" +
                "      (if (null? l)\n" +
                "          '()\n" +
                "          (cons (car l)\n" +
                "                (sieve (remove-multiples (car l) (cdr l))))))))\n" +
                "\n" +
                "(define primes<=\n" +
                "  (lambda (n)\n" +
                "    (sieve (interval-list 2 n))))\n" +
                "\n" +
                "\n" +
                "(define loop\n" +
                "  (lambda (n)\n" +
                "    (primes<= 10000)\n" +
                "    (if (> n 0) (loop (- n 1)))))\n" +
                "\n" +
                "\n" +
                "(loop 10000)\n" +
                "(loop 10000)\n" +
                "(loop 10000)\n" +
                "(loop 10000)\n" +
                "(loop 10000)\n" +
                "(loop 10000)\n" +
                "\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(loop 10000)\n" +
                "\n" +
                "(define end (current-milliseconds))\n" +
                "(- end start)\n" +
                "\n" +
                "\n";

        var result  = context.eval("scm", program);
        System.out.println(result);

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
    }

    @Test
    public void test10() {
        var program = "" +
                "(define fibonacci\n" +
                "  (lambda (n)\n" +
                "    (if (< n 2)\n" +
                "        1\n" +
                "        (+ (fibonacci (- n 1))\n" +
                "           (fibonacci (- n 2))))))\n" +
                "           \n" +
                "(fibonacci 30)\n" +
                "(fibonacci 30)\n" +
                "(fibonacci 30)\n" +
                "(fibonacci 30)\n" +
                "(fibonacci 30)\n" +
                "(fibonacci 30)\n" +
                "(fibonacci 30)\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(fibonacci 30)\n" +
                "\n" +
                "(define end (current-milliseconds))" +
                "(- end start)";
        var result  = context.eval("scm", program);
    }

    @Test
    public void test11() {
        var program = "" +
                "(define fibonacci\n" +
                "  (lambda (n)\n" +
                "    (if (< n 2)\n" +
                "        n\n" +
                "        (+ (fibonacci (- n 1))\n" +
                "           (fibonacci (- n 2))))))\n" +
                "           \n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(fibonacci 32)\n" +
                "\n" +
                "(define end (current-milliseconds))" +
                "(- end start)";

        var test = context.eval("scm", program);
        System.out.println(test);
    }

    @Test
    public void test12() {
        var program = " (eval '((lambda (x) (+ x 5)) 5))";

        var test = context.eval("scm", program);

        assertEquals(10L, test.asLong());
    }

    @Test
    public void test13() {
        var program = "(define fun (lambda (x) (eval '(define y 10)) (+ x y))) (fun 5)";

        var test = context.eval("scm", program);

        assertEquals(15L, test.asLong());
    }

    @Test
    public void test14() {
        var program = "" +
                "(eval '(define fibonacci\n" +
                "  (lambda (n)\n" +
                "    (if (< n 2)\n" +
                "        n\n" +
                "        (+ (fibonacci (- n 1))\n" +
                "           (fibonacci (- n 2)))))))\n" +
                "           \n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(fibonacci 32)\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(fibonacci 32)\n" +
                "\n" +
                "(define end (current-milliseconds))" +
                "(- end start)";

        var test = context.eval("scm", program);
    }

    @Test
    public void test15() {
        var program = "(define fun (lambda (x y) (+ x y))) (fun 1 2) (fun 2 3) (fun 3 4)";

        var test = context.eval("scm", program);
    }
}
