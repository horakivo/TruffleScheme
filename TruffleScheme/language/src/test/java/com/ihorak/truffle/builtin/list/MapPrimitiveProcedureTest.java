package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.convertor.callable.BuiltinConverter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@Ignore
public class MapPrimitiveProcedureTest {

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
    public void givenListAndMinus_whenMap_thenShouldListWithNegativeNumbers() {
        var program = "(map - (list 1 2 3))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3, result.getArraySize());
        assertEquals(-1L, result.getArrayElement(0).asLong());
        assertEquals(-2L, result.getArrayElement(1).asLong());
        assertEquals(-3L, result.getArrayElement(2).asLong());
    }

    @Test
    public void givenListsAndPlus_whenMap_thenShouldReturnAddedAllLists() {
        var program = "(map + (list 1 2 3) (list 4 5 6) (list 7 8 9))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3, result.getArraySize());
        assertEquals(12L, result.getArrayElement(0).asLong());
        assertEquals(15L, result.getArrayElement(1).asLong());
        assertEquals(18L, result.getArrayElement(2).asLong());
    }

    @Test
    public void givenListsWithDifferentNumberOfArgs_whenMap_thenShouldThrowException() {
        var program = "(map + (list 1 2) (list 4 5 6) (list 7 8 9))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("map: contract violation\n" +
                "all lists have to have same length", msg);
    }

    @Test
    public void givenNumberAsArgument_whenMap_thenShouldThrowException() {
        var program = "(map + 5 (list 4 5 6) (list 7 8 9))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("map: contract violation\n" +
                "expected: lists?\n" +
                "given: 5", msg);
    }

    @Test
    public void givenNumberAsProcedure_whenMap_thenShouldThrowException() {
        var program = "(map 5 (list 1 2 3) (list 4 5 6) (list 7 8 9))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("map: contract violation\n" +
                "expected: procedure?\n" +
                "given: 5", msg);
    }

    @Test
    public void givenProcedureWithDifferentNumberOfArgs_whenMap_thenShouldThrowException() {
        var program = "(map (lambda (x) (- x)) (list 1 2 3) (list 4 5 6) (list 7 8 9))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("User defined procedure: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 1\n" +
                "given: 3", msg);
    }

    @Test
    public void givenAppendProcedure_whenMap_thenShouldReturnCorrectResult() {
        var program = "(map append '((1 2) (3 4)) '((5 6) (7 8)))";

        var result = context.eval("scm", program);

        assertEquals("((1 2 5 6) (3 4 7 8))", result.toString());
    }

    @AfterClass
    public static void after() {
        BuiltinConverter.isBuiltinEnabled = true;
    }
}
