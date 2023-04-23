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

//    @Test
//    public void dasdsa() {
//        var program = """
//                ()
//                """;
//
//
//        var result = context.eval("scm", program);
//
//        assertEquals(0L, result.asLong());
//    }

}
