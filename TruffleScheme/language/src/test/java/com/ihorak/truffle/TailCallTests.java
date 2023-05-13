package com.ihorak.truffle;

import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TailCallTests {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenProcedureInTailCallPosition_whenCalled_correctResultIsReturned() {
        var program = """
                (define some-proc
                  (lambda (n)
                    n))
                                
                (define tco-procedure
                  (lambda (n)
                    (if (<= n 0)
                        n
                        (some-proc (- n 1)))))
                        
                (tco-procedure 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(9L, result.asLong());
    }

    @Test
    public void givenTCOProcedureInNonTailCallPosition_whenCalled_thenCatcherIsCreatedAndCorrectResultReturned() {
        // idea is that if we define tco-procedure before its use we know it is a TCO procedure therefore the callable
        // in the some-proc can be a catcher
        var program = """              
                (define tco-procedure
                  (lambda (n)
                    (if (<= n 0)
                        n
                        (some-proc (- n 1)))))
                                
                (define some-proc
                  (lambda (n)
                    (tco-procedure n)
                    (+ 1 1)))
                    
                                
                        
                (some-proc 10)
                                
                """;

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }

    @Test
    public void givenTCOProcedureDefinedAfterItsUsage_whenCalled_thenExceptionIsNotEscaping() {
        // the idea here is that we are calling `tco-procedure` before it's definition, so we have to
        // assume that the procedure is TCO
        var program = """      
                (define some-proc
                  (lambda (n)
                    (tco-procedure n)
                    (+ 1 1)))
                                
                        
                (define tco-procedure
                  (lambda (n)
                    (if (<= n 0)
                        n
                        (some-proc (- n 1)))))
                        
                (some-proc 10)
                                
                """;

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }

    @Test
    public void tailCallRecursionIsCorrectlyRecognized() {
        var program = """
                (define ivo
                  (lambda (n)
                    (let ((x (if (< n 0) (return-t) (return-f))))
                      5)))
                                
                (define return-f
                  (lambda ()
                    #f))
                                
                                
                (define return-t
                  (lambda ()
                    #t))
                                
                        
                (ivo 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void tailCallRecursionIsCorrectlyRecognizedInAndExpression() {
        var program = """
                (define ivo
                  (lambda (n)
                    (and 5 4 (return-t))
                      n))
                               
                (define return-t
                  (lambda ()
                    #t))
                                
                        
                (ivo 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

    @Test
    public void tailCallRecursionIsCorrectlyRecognizedInOrExpression() {
        var program = """
                (define ivo
                  (lambda (n)
                    (or #f (return-t))
                      n))
                               
                (define return-t
                  (lambda ()
                    #t))
                                
                        
                (ivo 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

    @Test
    public void tailCallRecursionIsCorrectlyRecognizedInCondExpression() {
        var program = """
                (define ivo
                  (lambda (n)
                    (cond (#f (return-t))
                          (#t (return-t)))
                    n))
                                
                               
                (define return-t
                  (lambda ()
                    #t))
                                
                        
                (ivo 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

    @Test
    public void chainTCOCalls() {
        var program = """
                (define trampoline
                  (lambda ()
                    (foo)))
                                
                (define foo
                  (lambda ()
                    (bar)))
                                
                (define bar
                  (lambda ()
                    (baz)))
                                
                (define baz
                  (lambda ()
                    10))
                                
                (trampoline)
                """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

}
