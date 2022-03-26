package com.ihorak.truffle.builtin.pair;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PairTest {

    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }

    @Test
    public void givenValidPair_whenQuoted_thenCorrectResultIsReturned() {
        var program = "'(1 2 3 . 2)";

        var result = context.eval("scm", program);

        assertTrue(result.hasMembers());
        assertEquals(1L, result.getMember("first").asLong());
        assertEquals(2L, result.getMember("second").getMember("first").asLong());
        assertEquals(3L, result.getMember("second").getMember("second").getMember("first").asLong());
        assertEquals(2L, result.getMember("second").getMember("second").getMember("second").asLong());
        assertEquals("(1 . (2 . (3 . 2)))", result.toString());
    }

    @Test
    public void givenInvalidPair_whenParsed_thenShouldThrowException() {
        var program = "(1 . 2 3)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("org.antlr.v4.runtime.misc.ParseCancellationException: line 1:7 extraneous input '3' expecting ')'", msg);
    }
}
