package com.ihorak.truffle.special_form;

import com.ihorak.truffle.GlobalEnvironment;
import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.node.exprs.ProcedureCallExprNode;
import com.ihorak.truffle.parser.Reader;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.Truffle;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LambdaExprNodeTest {

    @Test
    public void givenLambdaExpr_whenExecuted_thenShouldReturnSchemeFunction() {
        var program = "(lambda (x) (+ x 2) (+ x 3) #f)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();


        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        //TODO I am not sure how to test more since I don't have access to rootNode and expressions inside
        assertEquals(SchemeFunction.class, result.getClass());
        assertNotNull(((SchemeFunction) result).getExpectedNumberOfArgs());
        assertEquals(1, (int) ((SchemeFunction) result).getExpectedNumberOfArgs());
    }

    @Test
    public void givenLambdaExpr_whenApplied_thenShouldCorrectResult() {
        var program = "((lambda (x) x) 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();


        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(5L, result);
    }

    @Test
    public void givenLambdaExpr_whenApplied_thenAllBuiltinProcedureShouldBeConverted() {
        var program = "((lambda (x) x) (+ (+ 1 2) 5))";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();


        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(8L, result);
        assertEquals(ProcedureCallExprNode.class, expr.getClass());
        var procedureExprCall = (ProcedureCallExprNode) expr;
        assertEquals(1, procedureExprCall.getArguments().length);
        //assertEquals(1 ,procedureExprCall.getArguments()[0].);
    }

    @Test
    public void givenAddLambdaExpr_whenApplied_thenAllArgumentsShouldBeAdded() {
        var program = "((lambda (x y z) (+ x y z)) 1 2 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();


        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(6L, result);
    }

    @Test
    public void givenMinusLambdaExpr_whenApplied_thenAllArgumentsShouldBeSubtracted() {
        var program = "((lambda (x y z) (- x y z)) 1 2 3)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();


        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(-4L, result);
    }

    @Test
    public void givenMinusLambdaExprWithOneArg_whenApplied_thenNumberShouldBeNegated() {
        var program = "((lambda (x) (- x)) 4)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();


        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(-4L, result);
    }

    @Test
    public void givenComplexLambdaExpr_whenApplied_thenCorrectResultShouldBeReturned() {
        var program = "((lambda (x) (+ ((lambda () (+ 1 2))) x)) 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(8L, result);
    }

    @Test
    public void givenNestedLambda_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) ((lambda (y) (+ x y)) 5)) 10)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(15L, result);
    }

    @Test
    public void givenDefinedNestedLambda_whenExecuted_thenCorrectResultIsReturned() {
        var program = "(define fun (lambda (x) ((lambda (y) (+ x y)) 1))) (fun 5) (fun 20)";
        var rootNode = Reader.readProgram(CharStreams.fromString(program));

        var result = Truffle.getRuntime().createDirectCallNode(rootNode.getCallTarget()).call();

        assertEquals(21L, result);
    }

//    @Test
//    public void fast_test_2() {
//        var program = "((lambda (x) (define plus +) (plus x 5)) 5)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(10L, result);
//    }
//
//    @Test
//    public void fast_test_3() {
//        var program = "((lambda (x) (define plus +) (+ 1 2) (plus x 5)) 5)";
//        var expr = Reader.readExpr(CharStreams.fromString(program));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//        assertEquals(10L, result);
//    }

    @Test
    public void givenLambdaWithEvalAndQuote_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) (eval '(define y 10)) (+ x y)) 5)";
        var context = new Context(null);
        var expr = Reader.test(CharStreams.fromString(program), context);
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(Truffle.getRuntime().createVirtualFrame(new Object[]{}, context.getFrameDescriptor()));
        assertEquals(15L, result);
    }

    @Test
    public void givenLambdaWithDefine_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) (define y 10) (+ x y)) 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(15L, result);
    }

    @Test
    public void givenLambdaWithRedefinitionOfLocalVariable_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) (define x 10) x) 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(10L, result);
    }

    @Test
    public void givenNestedLambdaWithDefine_whenExecuted_thenCorrectResultIsReturned() {
        var program = "((lambda (x) ((lambda (y) (define y 10) (+ x y)) 20)) 5)";
        var expr = Reader.readExpr(CharStreams.fromString(program));
        GlobalEnvironment globalEnvironment = new GlobalEnvironment();

        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
        assertEquals(15L, result);
    }

//    @Test
//    public void fast_test_4() throws IOException, RecognitionException {
//        var program = "(lambda (x) )";
//        var expr = Reader.read(new ByteArrayInputStream(program.getBytes()));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//
//
//        var result = expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//    }
}
