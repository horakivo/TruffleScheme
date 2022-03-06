package com.ihorak.truffle.builtin.logical;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LessThenOrEqualExprNodeTest {


    private Context context;

    @Before
    public void setUp() {
        context = Context.create();
    }


    @Test
    public void givenNoArgs_whenExecuted_thenExceptionShouldBeThrown() {
        var program = "(<=)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("<=: arity mismatch; Expected number of argument does not match the given number\n" +
                "expected: at least 1\n" +
                "given: 0", msg);
    }

    @Test
    public void givenOneArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(<= 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertTrue((boolean) result);
    }

    @Test
    public void givenTwoArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(<= 3 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertTrue((boolean) result);
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnTrue() {
        var program = "(<= 3 4 5 6 7 8 9 10)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertTrue((boolean) result);
    }

    @Test
    public void givenArbitraryArg_whenExecuted_thenShouldReturnFalse() {
        var program = "(<= 3 4 6 5 7 8 9 10)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());

        assertFalse((boolean) result);
    }

    @Test
    public void givenWrongArgumentType_whenExecuted_shouldThrowException() {
        var program = "(<= 3 4 'a 5 7 8 9 10)";

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("<=: contract violation. Unsupported types! Left: 4 Right: 'a", msg);
    }

}
