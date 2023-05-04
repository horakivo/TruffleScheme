package com.ihorak.truffle;

import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CallableTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @After
    public void tearDown() {
        this.context.close();
    }

    @Test
    public void givenPrimitiveProcedure_whenRedefined_CorrectResultIsReturned() {
        var program = """
                (define add (lambda (a b) (+ a b)))
                
                (define a (add 1 2))
                (define + -)
                (define b (add 1 2))
                
                (list a b)
                """;


        var result = context.eval("scm", program);

        assertEquals(3L, result.getArrayElement(0).asLong());
        assertEquals(-1L, result.getArrayElement(1).asLong());
    }
}
