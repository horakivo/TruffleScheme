package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.convertor.callable.BuiltinConverter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class AppendPrimitiveProcedureTest {

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
    public void givenNoArgs_whenAppend_thenShouldReturnEmptyList() {
        var program = "(append)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(0L, result.getArraySize());
    }

    @Test
    public void givenOneList_whenAppend_thenShouldReturnGivenList() {
        var program = "(append (list 1 2))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(2L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
    }

    @Test
    public void givenTwoList_whenAppend_thenShouldReturnMergedLists() {
        var program = "(append (list 1 2) (list 3 4))";

        var result = context.eval("scm", program);


        assertTrue(result.hasArrayElements());
        assertEquals(4L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenArbitraryNumberOfArgs_whenAppend_thenShouldReturnEmptyList() {
        var program = "(append (list 1 2) (list 3 4) (list 5 6))";


        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(6L, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
        assertEquals(5L, result.getArrayElement(4).asLong());
        assertEquals(6L, result.getArrayElement(5).asLong());
    }

    @Test
    public void givenOneListOnePair_whenAppend_thenShouldThrowException() {
        var program = "(append (list 1 2) (cons 3 4))";


        var msg = assertThrows(PolyglotException.class, () ->  context.eval("scm", program)).getMessage();

        assertEquals("append: contract violation\n" +
                "expecting all arguments lists\n" +
                "given: (3 . 4)", msg);
    }

    @AfterClass
    public static void after() {
        BuiltinConverter.isBuiltinEnabled = true;
    }
}
