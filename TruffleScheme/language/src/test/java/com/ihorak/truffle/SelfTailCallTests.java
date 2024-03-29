package com.ihorak.truffle;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelfTailCallTests {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void selfTCOisCorrectlyRecognizedIfWrappedInLet() {
        var program = """
                (define countdown
                  (lambda (n)
                    (let ((number n))
                      (if (= 0 number)
                        0
                        (countdown (- number 1))))))
                        
                (countdown 10)
                """;


        var result = context.eval("scm", program);

        assertEquals(0L, result.asLong());
    }



    @Test
    public void selfTCOisCorrectlyRecognizedIfWrappedInAnd() {
        var program = """
                (define countdown
                  (lambda (n)
                    (if (= 0 n)
                        0
                        (and 5 (countdown (- n 1))))))
                        
                (countdown 10)
                """;


        var result = context.eval("scm", program);

        assertEquals(0L, result.asLong());
    }

    @Test
    public void selfTCOisCorrectlyRecognizedIfWrappedInOr() {
        var program = """
                (define countdown
                  (lambda (n)
                    (if (= 0 n)
                        0
                        (or #f (countdown (- n 1))))))
                        
                (countdown 10)
                """;


        var result = context.eval("scm", program);

        assertEquals(0L, result.asLong());
    }

    @Test
    public void givenSelfTCO_whenProcedureShadowedByLet_thenSelfTailThrowerIsNotCreated() {
        var program = """
                (define countdown
                  (lambda (n)
                    (let ((countdown (lambda (n) 11)))
                      (if (= n 0)
                          0
                          (countdown (- n 1))))))
                        
                (countdown 10)
                """;


        var result = context.eval("scm", program);

        assertEquals(11L, result.asLong());
    }

    @Test
    public void givenSelfTCO_whenProcedureShadowedByLetrec_thenSelfTailThrowerIsNotCreated() {
        var program = """
                (define countdown
                  (lambda (n)
                    (letrec ((countdown (lambda (n) 11)))
                      (if (= n 0)
                          0
                          (countdown (- n 1))))))
                        
                (countdown 10)
                """;


        var result = context.eval("scm", program);

        assertEquals(11L, result.asLong());
    }

    @Test
    public void givenSelfTCO_whenProcedureShadowedByDefine_thenSelfTailThrowerIsNotCreated() {
        var program = """
                (define countdown
                  (lambda (n)
                    (define countdown (lambda (n) 11))
                    (if (= n 0)
                        0
                        (countdown (- n 1)))))
                                
                (countdown 10)
                """;


        var result = context.eval("scm", program);

        assertEquals(11L, result.asLong());
    }



}
