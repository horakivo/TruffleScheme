package com.ihorak.truffle.benchmarks;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BenchmarkTests {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void fib() {
        var program = """
                (define fibonacci
                  (lambda (n)
                      (if (< n 2)
                          n
                          (+ (fibonacci (- n 1))
                             (fibonacci (- n 2))))))
                                
                                
                                
                (fibonacci 20)
                """;


        var result = context.eval("scm", program);

        assertEquals(6765, result.asLong());
    }

    @Test
    public void tak() {
        var program = """
                (define tak
                  (lambda (x y z)
                    (if (not (< y x))
                        z
                        (tak (tak (- x 1) y z)
                           (tak (- y 1) z x)
                           (tak (- z 1) x y)))))
                                
                                
                (tak 18 12 6)
                """;

        var result = context.eval("scm", program);

        assertEquals(7, result.asLong());
    }

    @Test
    public void random_list() {
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

                (random-list 5)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(5L, result.getArraySize());
        assertEquals("(256 1531 1410 81 9)", result.toString());
    }


    @Test
    public void random_list_3_fun() {
        var program = """
                (define random-list
                  (lambda (len)
                    (generate len 101 17 3 '())))
                                
                                
                (define generate
                  (lambda (len p q s result)
                    (if (= len 0)
                        result
                        (tmp (modulo (* s s) (* p q)) len p q result))))
                                
                                
                (define tmp
                  (lambda (value len p q result)
                           (generate (- len 1) p q value (cons value result))))

                (random-list 5)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(5L, result.getArraySize());
        assertEquals("(256 1531 1410 81 9)", result.toString());
    }

    @Test
    public void quicksort() {
        var program = """
                                
                (define pivot
                  (lambda (l)
                    (cond ((null? l) 'done)
                          ((null? (cdr l)) 'done)
                          ((<= (car l) (car (cdr l))) (pivot (cdr l)))
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
                                    (quicksort1 (car (cdr parts)))))))))
                                
                                
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
                                
                                
                (quicksort1 (random-list 10))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(10L, result.getArraySize());
        assertEquals(9L, result.getArrayElement(0).asLong());
        assertEquals(81L, result.getArrayElement(1).asLong());
        assertEquals(239L, result.getArrayElement(2).asLong());
        assertEquals(256L, result.getArrayElement(3).asLong());
        assertEquals(290L, result.getArrayElement(4).asLong());
        assertEquals(1089L, result.getArrayElement(5).asLong());
        assertEquals(1191L, result.getArrayElement(6).asLong());
        assertEquals(1410L, result.getArrayElement(7).asLong());
        assertEquals(1531L, result.getArrayElement(8).asLong());
        assertEquals(1684L, result.getArrayElement(9).asLong());
    }

    @Test
    public void quicksort_3_functions() {
        var program = """
                (define pivot
                  (lambda (l)
                    (cond ((null? l) 'done)
                          ((null? (cdr l)) 'done)
                          ((<= (car l) (car (cdr l))) (pivot (cdr l)))
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
                                    (quicksort1 (car (cdr parts)))))))))

                (define random-list
                  (lambda (len)
                    (generate len 101 17 3 '())))


                (define generate
                  (lambda (len p q s result)
                    (if (= len 0)
                        result
                        (tmp (modulo (* s s) (* p q)) len p q result))))


                (define tmp
                  (lambda (value len p q result)
                           (generate (- len 1) p q value (cons value result))))
                                
                (quicksort1 (random-list 10))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(10L, result.getArraySize());
        assertEquals(9L, result.getArrayElement(0).asLong());
        assertEquals(81L, result.getArrayElement(1).asLong());
        assertEquals(239L, result.getArrayElement(2).asLong());
        assertEquals(256L, result.getArrayElement(3).asLong());
        assertEquals(290L, result.getArrayElement(4).asLong());
        assertEquals(1089L, result.getArrayElement(5).asLong());
        assertEquals(1191L, result.getArrayElement(6).asLong());
        assertEquals(1410L, result.getArrayElement(7).asLong());
        assertEquals(1531L, result.getArrayElement(8).asLong());
        assertEquals(1684L, result.getArrayElement(9).asLong());
    }

    @Test
    public void quicksort_generate_function_inlined() {
        var program = """
                (define pivot
                  (lambda (l)
                    (cond ((null? l) 'done)
                          ((null? (cdr l)) 'done)
                          ((<= (car l) (car (cdr l))) (pivot (cdr l)))
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
                                    (quicksort1 (car (cdr parts)))))))))

                (define random-list
                  (lambda (len)
                    (generate len 101 17 3 '())))


                (define generate
                  (lambda (len p q s result)
                    (if (= len 0)
                        result
                        (let ((value (modulo (* s s) (* p q))))
                          (generate (- len 1) p q value (cons value result))))))
                                
                (quicksort1 (random-list 10))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(10L, result.getArraySize());
        assertEquals(9L, result.getArrayElement(0).asLong());
        assertEquals(81L, result.getArrayElement(1).asLong());
        assertEquals(239L, result.getArrayElement(2).asLong());
        assertEquals(256L, result.getArrayElement(3).asLong());
        assertEquals(290L, result.getArrayElement(4).asLong());
        assertEquals(1089L, result.getArrayElement(5).asLong());
        assertEquals(1191L, result.getArrayElement(6).asLong());
        assertEquals(1410L, result.getArrayElement(7).asLong());
        assertEquals(1531L, result.getArrayElement(8).asLong());
        assertEquals(1684L, result.getArrayElement(9).asLong());
    }


    @Test
    public void fak() {
        var program = """
                (define fak
                  (lambda (x)
                    (define iter
                      (lambda (n result)
                        (if (= n 0)
                          result
                          (iter (- n 1) (+ n result)))))
                          
                    (iter x 1)))
                    
                (fak 1000)
                """;

        var result = context.eval("scm", program);

        assertEquals(500501L, result.asLong());
    }

    @Test
    public void primes() {
        var program = """
                                
                (define interval-list
                  (lambda (m n)
                    (if (> m n)
                        '()
                        (cons m (interval-list (+ 1 m) n)))))
                                
                (define sieve
                  (lambda  (l)
                    (letrec ((remove-multiples
                              (lambda (n l)
                                (if (null? l)
                                    '()
                                    (if (= (modulo (car l) n) 0)
                                        (remove-multiples n (cdr l))
                                        (cons (car l)
                                              (remove-multiples n (cdr l))))))))
                      (if (null? l)
                          '()
                          (cons (car l)
                                (sieve (remove-multiples (car l) (cdr l))))))))
                                
                (define primes<=
                  (lambda (n)
                    (sieve (interval-list 2 n))))
                    
                (primes<= 10)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(4L, result.getArraySize());
        assertEquals(2L, result.getArrayElement(0).asLong());
        assertEquals(3L, result.getArrayElement(1).asLong());
        assertEquals(5L, result.getArrayElement(2).asLong());
        assertEquals(7L, result.getArrayElement(3).asLong());
    }
}
