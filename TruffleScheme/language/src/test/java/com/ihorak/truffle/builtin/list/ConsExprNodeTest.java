package com.ihorak.truffle.builtin.list;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsExprNodeTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenNumbers_whenCons_thenReturnSchemePair() {
        var program = "(cons 1 2)";

        var result = context.eval("scm", program);

        assertTrue(result.hasMembers());
        assertEquals(1L, result.getMember("first").asLong());
        assertEquals(2L, result.getMember("second").asLong());
        assertEquals("(1 . 2)", result.toString());
    }

    @Test
    public void givenWrongNumberOfArgs_whenCons_thenExceptionIsThrown() {
        var program = "(cons 1)";

        var msg = assertThrows(PolyglotException.class, () ->  context.eval("scm", program)).getMessage();

        assertEquals("cons: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 2\n" +
                "given: 1", msg);
    }

    @Test
    public void givenComplexStructure_whenCons_thenReturnCorrectValue() {
        var program = "(cons (cons 1 2) (cons 3 4))";


        var result =  context.eval("scm", program);

        assertTrue(result.hasMembers());
        assertTrue(result.getMember("first").hasMembers());
        assertTrue(result.getMember("second").hasMembers());
        assertEquals(1L, result.getMember("first").getMember("first").asLong());
        assertEquals(2L, result.getMember("first").getMember("second").asLong());
        assertEquals(3L, result.getMember("second").getMember("first").asLong());
        assertEquals(4L, result.getMember("second").getMember("second").asLong());
        assertEquals("((1 . 2) . (3 . 4))", result.toString());
    }

    @Test
    public void givenLastElementEmptyList_whenCons_thenResultList() {
        var program = "(cons (cons 1 2) (list))";


        var result = context.eval("scm", program);

        assertEquals("((1 . 2))", result.toString());
    }

    @Test
    public void givenLastElementList_whenCons_thenResultList() {
        var program = "(cons (cons 1 2) (list 1 2))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3L, result.getArraySize());
        assertTrue(result.getArrayElement(0).hasMembers());
        assertEquals(1L, result.getArrayElement(0).getMember("first").asLong());
        assertEquals(2L, result.getArrayElement(0).getMember("second").asLong());
        assertEquals(1L, result.getArrayElement(1).asLong());
        assertEquals(2L, result.getArrayElement(2).asLong());
        assertEquals("((1 . 2) 1 2)", result.toString());
    }
}
