package com.ihorak.truffle.builtin.list;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CdrExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenList_whenCdr_thenReturnSecondElementOfList() {
        var program = "(cdr (list 1 2))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(2L, result.getArrayElement(0).asLong());
    }

    @Test
    public void givenPair_whenCdr_thenReturnSecondElementOfPair() {
        var program = "(cdr (cons 1 2))";

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }

    @Test
    public void givenNumber_whenCdr_thenUnsupportedSpecializationExceptionShouldBeThrown() {
        var program = "(cdr 1)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("cdr: contract violation\n" +
                "expected: pair?\n" +
                "given: 1", msg);
    }

    @Test
    public void givenEmptyList_whenCdr_thenSchemeExceptionShouldBeThrown() {
        var program = "(cdr (list))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("cdr: contract violation \n" +
                " expected: pair? \n" +
                " given: ()", msg);
    }
}
