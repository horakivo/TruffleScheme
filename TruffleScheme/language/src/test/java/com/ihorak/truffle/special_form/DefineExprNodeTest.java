package com.ihorak.truffle.special_form;

import com.ihorak.truffle.parser.Reader;
import com.oracle.truffle.api.Truffle;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefineExprNodeTest {

    @Test
    public void givenAnyValue_whenDefineIsExecuted_thenCorrectValueShouldBeStored() {
        var program = "(define x 5) x";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));

        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();
        assertEquals(5L, result);
    }

    @Test
    public void gasda() {
        var program = "(define foo (lambda (x) (+ x y))) (define y 10) (foo 5)";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));

        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();
        assertEquals(15L, result);
    }
}
