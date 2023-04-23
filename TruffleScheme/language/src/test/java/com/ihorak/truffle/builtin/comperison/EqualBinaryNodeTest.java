package com.ihorak.truffle.builtin.comperison;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EqualBinaryNodeTest {

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
    public void givenNoArgs_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(equal?)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                equal?: arity mismatch; Expected number of arguments does not match the given number
                expected: 2
                given: 0""", msg);
    }

    @Test
    public void givenOneArgument_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(equal? 1)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                equal?: arity mismatch; Expected number of arguments does not match the given number
                expected: 2
                given: 1""", msg);
    }

    @Test
    public void givenTwoSameLongNumbers_whenExecuted_thenShouldReturnTrue() {
        var program = "(equal? 3 3)";

        var result =  context.eval("scm", program);

        assertTrue(result.asBoolean());
    }


    @Test
    public void givenTwoDifferentLongNumbers_whenExecuted_thenShouldReturnFalse() {
        var program = "(equal? 3 4)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenLongAndBigInteger_whenExecuted_thenReturnFalse() {
        // Long max value = 9223372036854775807
        var program = "(equal? 3 " + "9223372036854775810" + " )";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenLongAndSymbol_whenExecuted_thenReturnFalse() {
        var program = "(equal? 3 'a)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenSymbolAndLong_whenExecuted_thenReturnFalse() {
        var program = "(equal? 'a 3)";

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenTwoSameSchemeList_whenExecuted_thenTrueIsReturned() {
        var program = "(equal? (list 1 2 'a) '(1 2 a))";

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoSameProcedure_whenExecuted_thenTrueIsReturned() {
        var program = """
                (define proc (lambda () 1))
                (equal? proc proc)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoDifferentProceduresWithSameBody_whenExecuted_thenFalseIsReturned() {
        var program = """
                (define proc (lambda () 1))
                (define proc1 (lambda () 1))
                (equal? proc proc1)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenTwoDifferentProcedures_whenExecuted_thenFalseIsReturned() {
        var program = """
               (define proc (lambda () 1))
               (define diff (lambda () 2))
               (equal? proc diff)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenTwoSameBooleans_whenExecuted_thenTrueIsReturned() {
        var program = """
               (equal? #t #t)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoDifferentBooleans_whenExecuted_thenFalseIsReturned() {
        var program = """
               (equal? #t #f)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenTwoSameStrings_whenExecuted_thenTrueIsReturned() {
        var program = """
               (equal? "aa" "aa")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoDifferentStrings_whenExecuted_thenFalseIsReturned() {
        var program = """
               (equal? "aa" "aaa")
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenTwoDifferentSchemePairs_whenExecuted_thenFalseIsReturned() {
        var program = """
               (equal? (cons 1 (cons 2 3)) (cons 1 (cons 3 4)))
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenTwoSameSchemePairs_whenExecuted_thenTrueIsReturned() {
        var program = """
               (equal? (cons 1 (cons 2 3))  (cons 1 (cons 2 3)))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoSamePrimitiveProcedures_whenExecuted_thenTrueIsReturned() {
        var program = """
               (equal? + +)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void givenTwoDifferentPrimitiveProcedures_whenExecuted_thenFalseIsReturned() {
        var program = """
               (equal? + -)
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void givenUndefinedValues_whenExecuted_thenTrueIsReturned() {
        var program = """
               (equal? (if #f 5) (if #f 4))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

}
