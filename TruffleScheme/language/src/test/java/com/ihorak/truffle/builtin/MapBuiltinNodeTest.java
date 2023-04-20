package com.ihorak.truffle.builtin;

import com.oracle.truffle.api.nodes.ExplodeLoop;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapBuiltinNodeTest {

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
    public void givenPrimitiveProcedureAndList_whenCalled_thenCorrectResultIsReturned() {
        var program = "(map - '(1 2 3))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(-1L, result.getArrayElement(0).asLong());
        assertEquals(-2L, result.getArrayElement(1).asLong());
        assertEquals(-3L, result.getArrayElement(2).asLong());
    }

    @Test
    public void givenUserDefinedProcedure_whenCalled_thenCorrectResultIsReturned() {
        var program = "(map (lambda (n) (- n)) '(1 2 3))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(-1L, result.getArrayElement(0).asLong());
        assertEquals(-2L, result.getArrayElement(1).asLong());
        assertEquals(-3L, result.getArrayElement(2).asLong());
    }

    @Test
    public void givenWrongNumberOfArgsInLists_whenCalled_thenExceptionIsThrown() {
        var program = "(map - (list 1 2) (list 1 2 3))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                map: all lists must have same size
                first list length: 2
                other list length: 3
                """, msg);
    }

    @Test
    public void givenWrongNumberOfArgs_whenCalled_thenExceptionIsThrown() {
        var program = "(map -)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                map: arity mismatch; Expected number of arguments does not match the given number
                expected: at least 2
                given: 1""", msg);
    }

}
