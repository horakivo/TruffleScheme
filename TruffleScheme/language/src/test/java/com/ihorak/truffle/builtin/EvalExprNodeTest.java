package com.ihorak.truffle.builtin;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeFunction;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EvalExprNodeTest {

    @Test
    public void givenRandomNumber_whenEvaluated_thenReturnCorrectResult() {
        var program = "(eval 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(5L, result);
    }

    @Test
    public void givenPlusExpr_whenEvaluated_thenReturnCorrectResult() {
        var program = "(eval (+ 1 2 3))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(6L, result);
    }

    @Test(expected = SchemeException.class)
    public void givenEvalWithWrongNumberOfArguments_whenExecuted_EvalShouldThrowException() {
        var program = "(eval 1 2)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
    }

    @Test
    public void givenLambdaExpr_whenEvaluated_thenSchemeFunctionShouldBeReturned() {
        var program = "(eval (lambda () (+ 1 2)))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(SchemeFunction.class, result.getClass());
    }

    @Test
    public void givenQuoteExprSymbol_whenEvaluated_thenListShouldBeEvaluatedCorrectly() {
        var program = "(eval 'a)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var exception = assertThrows(SchemeException.class, () -> expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame()));

        assertEquals("'a: undefined\n" +
                "cannot reference an identifier before its definition", exception.getMessage());
    }

    @Test
    public void givenQuoteExprList_whenEvaluated_thenListShouldBeEvaluatedCorrectly() {
        var program = "(eval '(+ 1 2))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(3L, result);
    }


//    @Test
//    public void givenLambdaExpr_whenExecuted_thenEvalShouldBeFoundInGlobalEnvAndExecutedCorrectly() {
//        var program = "((lambda () (define abc eval) (abc 8)))";
//        var expr = Reader.read(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(8L, result);
//    }

//    @Test(expected = SchemeException.class)
//    public void givenLambdaExprWithWrongNumberOfArguments_whenExecuted_EvalShouldThrowException() {
//        var program = "((lambda () (define abc eval) (abc 8 5)))";
//        var expr = Reader.read(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//    }
}
