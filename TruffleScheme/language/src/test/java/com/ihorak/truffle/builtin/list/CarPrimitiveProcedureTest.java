package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.convertor.util.BuiltinUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CarPrimitiveProcedureTest {

    private Context context;

    @BeforeClass
    public static void before() {
        BuiltinUtils.isBuiltinEnabled = false;
    }

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenList_whenCar_thenReturnFirstElementOfList() {
        var program = "(car (list 1 2))";

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void givenFirstElementList_whenCar_thenReturnFirstElementOfList() {
        var program = "(car (list (list 1) 2))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(1L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
    }

    @Test
    public void givenPair_whenCar_thenReturnFirstElementOfPair() {
        var program = "(car (cons 1 2))";


        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void givenWrongNumberOfArgs_whenCar_thenShouldThrowException() {
        var program = "(car (cons 1 2) (cons 3 4))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("" +
                "car: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 1\n" +
                "given: 2", msg);
    }

    @Test
    public void givenNumber_whenCar_thenUnsupportedSpecializationExceptionShouldBeThrown() {
        var program = "(car 1)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("car: contract violation\n" +
                "expected: pair? or list?\n" +
                "given: 1", msg);
    }

    @Test
    public void givenEmptyList_whenCar_thenSchemeExceptionShouldBeThrown() {
        var program = "(car (list))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("car: contract violation\n" +
                "expected: pair? or list?\n" +
                "given: ()", msg);
    }

    @AfterClass
    public static void after() {
        BuiltinUtils.isBuiltinEnabled = true;
    }
}
