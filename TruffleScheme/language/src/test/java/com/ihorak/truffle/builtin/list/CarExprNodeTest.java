package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import org.antlr.v4.runtime.CharStreams;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarExprNodeTest {

    private Context context;

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
    public void givenNumber_whenCar_thenUnsupportedSpecializationExceptionShouldBeThrown() {
        var program = "(car 1)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("car: contract violation\n expected: pair?\n given: 1", msg);
    }

    @Test
    public void givenEmptyList_whenCar_thenSchemeExceptionShouldBeThrown() {
        var program = "(car (list))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("car: contract violation\nexpected: pair?\ngiven: ()", msg);
    }
}
