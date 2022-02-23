package com.ihorak.truffle.builtin;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.Reader;
import com.oracle.truffle.api.Truffle;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProgramTest {

    @Test
    public void test1() {
        var program = "(define fib (lambda (n) (if (<= n 2) 1 (+ (fib (- n 1)) (fib (- n 2)))))) (fib 30)";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();

        assertEquals(832040L, result);
    }

    @Test
    public void test2() {
        var program = "(define foo (lambda () x)) (define bar (lambda (x) (foo))) (bar 1)";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));

        var exceptionMsg = assertThrows(SchemeException.class, () -> Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call()).getMessage();
        assertEquals("'x: undefined\n" +
                "cannot reference an identifier before its definition. FrameSlotKind: Illegal", exceptionMsg);
    }

    @Test
    public void test3() {
        var program = "(define foo (lambda () x)) (define bar (lambda (x) (foo))) (define x 88) (bar 1)";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));

        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();
        assertEquals(88L, result);
    }
}
