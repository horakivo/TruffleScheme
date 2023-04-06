package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.convertor.callable.BuiltinConverter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LengthPrimitiveProcedureTest {

    private Context context;

    @BeforeClass
    public static void before() {
        BuiltinConverter.isBuiltinEnabled = false;
    }

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenListOfNumbers_whenLength_thenReturnSizeOfTheList() {
        var program = "(length (list 1 2))";

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }

    @Test
    public void givenPair_whenLength_thenExceptionShouldBeThrown() {
        var program = "(length (cons 1 2))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("length: contract violation\n" +
                "expected: list?\n" +
                "given: (1 . 2)", msg);
    }

    @Test
    public void givenNumber_whenLength_thenExceptionShouldBeThrown() {
        var program = "(length 5)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("length: contract violation\n" +
                "expected: list?\n" +
                "given: 5", msg);
    }

    @AfterClass
    public static void after() {
        BuiltinConverter.isBuiltinEnabled = true;
    }
}
