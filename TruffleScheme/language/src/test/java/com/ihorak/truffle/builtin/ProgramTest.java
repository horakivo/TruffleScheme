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

        var result = context.eval("scm", program);

    }

    @Test
    public void tak() {
        var program = """
                ;;; TAK -- A vanilla version of the TAKeuchi function.
                                
                (define tak
                  (lambda (x y z)
                    (if (not (< y x))
                        z
                        (tak (tak (- x 1) y z)
                           (tak (- y 1) z x)
                           (tak (- z 1) x y)))))
                                
                                
                (define loop
                  (lambda (n)
                    (tak 18 12 6)
                    (if (> n 0) (loop (- n 1)))))
                                
                                
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                                
                                
                                
                                
                (define start (current-milliseconds))
                                
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                (loop 100)
                                
                (define end (current-milliseconds))
                (- end start)
                                
                """;

        var result = context.eval("scm", program).asLong();
        System.out.println(result / 10D);

    }


    @Test
    public void quicksort() {
        var program = """
                               
                (define pivot
                  (lambda (l)
                    (cond ((null? l) 'done)
                          ((null? (cdr l)) 'done)
                          ((<= (car l) (cadr l)) (pivot (cdr l)))
                          (#t (car l)))))
                               
                               
                               
                (define partition
                  (lambda (piv l p1 p2)
                    (if (null? l) (list p1 p2)
                        (if (< (car l) piv)
                            (partition piv (cdr l) (cons (car l) p1) p2)
                            (partition piv (cdr l) p1 (cons (car l) p2))))))
                               
                (define quicksort1
                  (lambda (l)
                    (let ((piv (pivot l)))
                      (if (equal? piv 'done)
                          l
                          (let ((parts (partition piv l '() '())))
                            (append (quicksort1 (car parts))
                                    (quicksort1 (cadr parts))))))))
                               
                               
                (define random-list
                  (lambda (len)
                    (define generate
                      (lambda (len p q s result)
                        (if (= len 0)
                            result
                            ((lambda (value)
                               (generate (- len 1) p q value (cons value result)))
                             (modulo (* s s) (* p q))))))
                      (generate len 101 17 3 '())))
                               
                               
                               
                               
                (quicksort1 (random-list 50000))
                               
                """;

        var result = context.eval("scm", program);
        System.out.println(result);

    }



    @Test
    public void asdasda() {
        var program = """
                (define countdown
                  (lambda (n)
                    (if (= 0 n)
                        0
                        (countdown (- n 1)))))
                        
                (countdown 10)
                """;

        var result = context.eval("scm", program);
        System.out.println(result);

    }

    //TODO THIS_IS_WRONG
    @Test
    public void asdasassada() {
        var program = """
                (define countdown
                  (lambda (n)
                    n))
                    
                (if 3 (countdown 10) 69)
                """;

        var result = context.eval("scm", program);
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

        var result = context.eval("scm", program);
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
        System.out.println(result);
    }


    @Test
    public void test12() {
        var program = " (eval '((lambda (x) (+ x 5)) 5))";

        var test = context.eval("scm", program);

        assertEquals(10L, test.asLong());
    }

    @Test
    public void tmp_remove_me() {
        var program = """
                (define test
                  (lambda (n)
                    (if (<= n 0)
                        (return-n n)
                        (test (- n 1)))))
                """;

        var test = context.eval("scm", program);

        assertEquals(10L, test.asLong());
    }

    @Test
    public void tmp_remove_me2() {
        var program = """
                (define test
                  (lambda (n)
                    (if (<= n 0)
                        (return-n n)
                        (test (- n 1)))))
                """;

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
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(fibonacci 35)\n" +
                "(define start (current-milliseconds))\n" +
                "\n" +
                "(fibonacci 35)\n" +
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

    @Test
    public void asdsad() {
        var program = """
                (define fibonacci
                  (lambda (n)
                      (if (< n 2)
                          n
                          (+ (fibonacci (- n 1))
                             (fibonacci (- n 2))))))
                          \s
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                (fibonacci 35)
                                
                (define start (current-milliseconds))
                                
                (fibonacci 35)
                                
                (define end (current-milliseconds))
                (- end start)
                """;


        var result = context.eval("scm", program).asLong();
        System.out.println(result);
    }


    @Test
    public void randomListTest3ProblemQuestionMark() {
        var program = """   
                 (define return-given-value
                  (lambda (x)
                    x))
                            
                 (define generate
                   (lambda (len p q s result)
                     (if (= len 0)
                         (return-given-value result)
                         (generate (- len 1) p q (modulo (* s s) (* p q)) (cons (modulo (* s s) (* p q)) result)))))
                                
                                
                (define random-list
                  (lambda (len)
                    (generate len 101 17 3 '())))
                                
                                
                (generate 10 4 3 2 '())
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                                
                                
                (define start (current-milliseconds))
                                
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                (random-list 500000)
                                
                (define end (current-milliseconds))
                (- end start)
                """;


        var result = context.eval("scm", program).asLong();
        System.out.println(result / 10D);
    }

    @Test
    public void sadasd() {
        var program = """   
                                                         
                (define random-list
                  (lambda (len)
                    (define generate
                      (lambda (len p q s result)
                        (if (= len 0)
                            result
                            ((lambda (value)
                               (generate (- len 1) p q value (cons value result)))
                             (modulo (* s s) (* p q))))))
                      (generate len 101 17 3 '())))
                                
                                
                                
     
                                
                                
                (define start (current-milliseconds))
                                
                (random-list 500000)
                                
                (define end (current-milliseconds))
                (- end start)
                                
                                
                """;


        var result = context.eval("scm", program).asLong();
        System.out.println(result);
    }
}
