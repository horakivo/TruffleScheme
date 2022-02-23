package com.ihorak.truffle.builtin;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.parser.Reader;
import com.oracle.truffle.api.Truffle;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgramTest {

    @Test
    public void test1() {
        var program = "(define fib (lambda (n) (if (<= n 2) 1 (+ (fib (- n 1)) (fib (- n 2)))))) (fib 30)";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();

        assertEquals(832040L, result);
    }
}
