package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.convertor.callable.BuiltinConverter;
import org.graalvm.polyglot.Context;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlusPrimitiveProcedureTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @BeforeClass
    public static void before() {
        BuiltinConverter.isBuiltinEnabled = false;
    }


    @AfterClass
    public static void after() {
        BuiltinConverter.isBuiltinEnabled = true;
    }

    @Test
    public void givenSmallNumbers_whenAddThem_thenReturnCorrectResult() {
        var program = "(+ 1 2 3 4 5)";

        var result = context.eval("scm", program);

        assertEquals(15L, result.asLong());
    }

    @Test
    public void givenOneNumber_whenAddIsCalled_thenSameValueShouldBeReturned() {
        var program = "(+ 88)";

        var result = context.eval("scm", program);

        assertEquals(88L, result.asLong());
    }

    @Test
    public void givenNoNumber_whenAddIsCalled_thenZeroShouldBeReturned() {
        var program = "(+)";

        var result = context.eval("scm", program);

        assertEquals(0L, result.asLong());
    }

    @Test
    public void givenFloatingDecimalNumber_whenAddIsCalled_thenShouldReturnResult() {
        var program = "(+ 12.3 5.3 1.1)";

        var result = context.eval("scm", program);

        assertEquals(12.3D + 5.3 + 1.1, result.asDouble(), 0);
    }

//    @Test
//    public void aaa() {
//        var program = """
//                (define a (+ 1 2))
//                (define + (lambda (a b) b))
//                (define b (+ 2 3))
//                (list a b)
//                """;
//
//        var result = context.eval("scm", program);
//
//        assertEquals(3L, result.getArrayElement(0).asLong());
//        assertEquals(3L, result.getArrayElement(1).asLong());
//    }
}
