package com.ihorak.truffle.builtin;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ApplyBuiltinNodeTest {

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
        var program = "(apply + '(1 2 3))";

        var result = context.eval("scm", program);

        assertEquals(6L, result.asLong());
    }

    @Test
    public void givenPrimitiveProcedureWithOptionalArgs_whenCalled_thenCorrectResultIsReturned() {
        var program = "(apply + 1 2 '(3 4))";

        var result = context.eval("scm", program);

        assertEquals(10L, result.asLong());
    }

    @Test
    public void givenUserDefinedProcedure_whenCalled_thenCorrectResultIsReturned() {
        var program = "(apply (lambda (a b) (+ a b)) '(3 4))";

        var result = context.eval("scm", program);

        assertEquals(7L, result.asLong());
    }

    @Test
    public void givenUserDefinedProcedureWithOptionalArgs_whenCalled_thenCorrectResultIsReturned() {
        var program = "(apply (lambda (a b) (+ a b)) 3 '(4))";

        var result = context.eval("scm", program);

        assertEquals(7L, result.asLong());
    }

    @Test
    public void givenNotAProcedure_whenCalled_thenExceptionIsThrown() {
        var program = "(apply 3 '(4))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                Application: not a procedure
                Expected: procedure that can be applied to arguments
                Given: 3""", msg);
    }

    @Test
    public void givenNotAListAsLastArg_whenCalled_thenExceptionIsThrown() {
        var program = "(apply + 1 2 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                apply: contract violation
                expected: list?
                given: 3""", msg);
    }

    @Test
    public void givenWrongNumberOfArgs_whenCalled_thenExceptionIsThrown() {
        var program = "(apply +)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                apply: arity mismatch; Expected number of arguments does not match the given number
                expected: at least 2
                given: 1""", msg);
    }
}
