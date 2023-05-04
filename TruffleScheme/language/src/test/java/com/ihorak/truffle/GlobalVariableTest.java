package com.ihorak.truffle;

import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalVariableTest {

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
    public void whenGlobalVariableIsRedefined_correctResultIsReturned() {
        var program = """
                (define some-number 5)
                (define test (lambda () some-number))
                (define list1 (list (test)))
                
                (define some-number 10)
                (define result (append list1 (list (test))))
                
                result
                """;


        var result = context.eval("scm", program);

        assertEquals(5L, result.getArrayElement(0).asLong());
        assertEquals(10L, result.getArrayElement(1).asLong());
    }
}
