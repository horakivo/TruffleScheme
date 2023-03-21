package com.ihorak.truffle;

import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.parser.AntlrToAST;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
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

}
