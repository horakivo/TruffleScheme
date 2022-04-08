package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void giveListAndMinus_whenMap_thenShouldListWithNegativeNumbers() {
        var program = "(map - (list 1 2 3))";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3, result.getArraySize());
        assertEquals(-1L, result.getArrayElement(0).asLong());
        assertEquals(-2L, result.getArrayElement(1).asLong());
        assertEquals(-3L, result.getArrayElement(1).asLong());
    }

//    @Test
//    public void giveListsAndPlus_whenMap_thenShouldReturnAddedAllLists() {
//        var program = "(map + (list 1 2 3) (list 4 5 6) (list 7 8 9))";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        var expectedResult = new SchemeCell(12L, new SchemeCell(15L, new SchemeCell(18L, SchemeCell.EMPTY_LIST)));
//
//        assertEquals(expectedResult, result);
//    }
//
//    //TODO potentionally move to JUNIT 5 since its not nice to check error messages here
//    @Test(expected = SchemeException.class)
//    public void giveListsWithDifferentNumberOfArgs_whenMap_thenShouldThrowException() {
//        var program = "(map + (list 1 2) (list 4 5 6) (list 7 8 9))";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//    }

    @Test
    public void givenProcedureWithDifferentNumberOfArgs_whenMap_thenShouldThrowException() {
        var program = "(map (lambda (x) (- x)) (list 1 2 3) (list 4 5 6) (list 7 8 9))";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("User defined procedure: arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: 1\n" +
                "given: 3", msg);
    }
}
