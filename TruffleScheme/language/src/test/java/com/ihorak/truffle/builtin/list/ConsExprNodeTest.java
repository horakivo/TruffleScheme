package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.type.SchemeCell;
import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsExprNodeTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenRandomNumbers_whenCons_thenReturnSchemeCell() {
        var program = "(cons 1 2)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
    }

    @Test
    public void givenComplexStructure_whenCons_thenReturnCorrectValue() {
        var program = "(cons (cons 1 2) (cons 3 4))";


        var result =  context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertTrue(result.getArrayElement(0).hasArrayElements());
        assertEquals(1L, result.getArrayElement(0).getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(0).getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(1).getArrayElement(0).asLong());
        assertEquals(4L, result.getArrayElement(1).getArrayElement(1).asLong());
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
        var expectedResult = new SchemeCell(new SchemeCell(1L, 2L), new SchemeCell(1L, new SchemeCell(2L, SchemeCell.EMPTY_LIST)));

        assertEquals(expectedResult, result);
        assertEquals("((1 . 2) 1 2)", result.toString());
    }
}
