package com.ihorak.truffle.builtin.list;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeCell;
import org.antlr.v4.runtime.CharStreams;
import org.graalvm.polyglot.Context;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListExprNodeTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenSomeArgs_whenListCreated_thenReturnCorrectSchemeCell() {
        var program = "(list 1 2 3 4)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }

    @Test
    public void givenNoArgs_whenEvaluated_thenReturnEmptyList() {
        var program = "(list)";

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(0L, result.getArraySize());
    }
}
