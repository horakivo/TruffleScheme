package com.ihorak.truffle.builtin.arithmetic;

import com.ihorak.truffle.convertor.callable.BuiltinConverter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@Ignore
public class DividePrimitiveProcedureTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @BeforeClass
    public static void before() {
        BuiltinConverter.isBuiltinEnabled = false;
    }

    @Test
    public void givenSmallNumber_whenDivideCalled_thenOneDividedByTheNumberShouldBeReturned() {
        var program = "(/ 4)";

        var result = context.eval("scm", program);

        assertEquals(1 / 4D, result.asDouble(), 0);
    }


    @Test
    public void givenNoNumber_whenDivideCalled_thenSchemeExceptionShouldBeThrown() {
        var program = "(/)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("/: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: at least 1\n" +
                "given: 0", msg);

    }

    @Test
    public void givenNumbers_whenDivideCalled_thenCorrectAnswerShouldBeReturn() {
        var program = "(/ 2 3 4)";


        var result = context.eval("scm", program);

        assertEquals(2 / 3D / 4, result.asDouble(), 0);
    }

    @Test
    public void givenNestedDivision_whenDivideCalled_thenCorrectAnswerShouldBeReturn() {
        var program = "(/ (/ 2 3 4) (/ 1 2))";


        var result = context.eval("scm", program);

        assertEquals(1 / 3D, result.asDouble(), 0);
    }

    @AfterClass
    public static void after() {
        BuiltinConverter.isBuiltinEnabled = true;
    }
}
