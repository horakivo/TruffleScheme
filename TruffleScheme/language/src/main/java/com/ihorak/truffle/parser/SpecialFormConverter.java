package com.ihorak.truffle.parser;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.special_form.*;
import com.ihorak.truffle.node.special_form.lambda.LambdaExprNode;
import com.ihorak.truffle.node.special_form.LetStarExprNode;
import com.ihorak.truffle.node.special_form.lambda.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.lambda.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class SpecialFormConverter {

    public static SchemeExpression convertListToSpecialForm(SchemeCell specialFormList, Context context) {
        var operationSymbol = (SchemeSymbol) specialFormList.car;
        switch (operationSymbol.getValue()) {
            case "if":
                return convertIf(specialFormList, context);
            case "define":
                return convertDefine(specialFormList, context);
            case "lambda":
                return convertLambda(specialFormList, context);
            case "quote":
                return convertQuote(specialFormList, context);
            case "quasiquote":
                return convertQuasiquote(specialFormList, context);
            case "let":
                return convertLet(specialFormList, context);
            case "let*":
                return convertLetStar(specialFormList, context);
            default:
                throw new IllegalArgumentException("Unknown special form");

        }
    }

    /*
     *                  IF
     *           /       |      \
     *        TEST  thenExpr   elseExpr?
     *
     * --> (if TEST thenExpr elseExpr?)
     * */
    private static IfExprNode convertIf(SchemeCell ifList, Context context) {
        //1st element is the symbol 'if'

        var test = ifList.get(1); // 2nd element
        var then = ifList.get(2); // 3rd element

        SchemeExpression testExpr = ListToExpressionConverter.convert(test, context);
        SchemeExpression thenExpr = ListToExpressionConverter.convert(then, context);
        SchemeExpression elseExpr = null;
        if (ifList.size() > 3) {
            var elseOptional = ifList.get(3); // 4th element
            elseExpr = ListToExpressionConverter.convert(elseOptional, context);
        }

        return new IfExprNode(testExpr, thenExpr, elseExpr);
    }

    /*
     *                 DEFINE
     *           /                \
     *        schemeSymbol       expr
     *
     * -->  (define variable expr)
     * */
    private static SchemeExpression convertDefine(SchemeCell defineList, Context context) {
        var potentialSymbol = defineList.get(1);

        if (potentialSymbol instanceof SchemeSymbol) {
            var symbol = (SchemeSymbol) potentialSymbol;
            SchemeExpression valueToStore = ListToExpressionConverter.convert(defineList.get(2), context);
            var index = context.findLocalSymbol(symbol);
            if (index != null) {
                return WriteLocalVariableExprNodeGen.create(valueToStore, index, symbol);
            } else {
                int frameIndex = context.addLocalSymbol(symbol);
                return WriteLocalVariableExprNodeGen.create(valueToStore, frameIndex, symbol);
            }
        } else {
            throw new ParserException("define: expected: Symbol \n given: " + potentialSymbol);
        }
    }

    /*
     *  --> (lambda (param1 .. paramN) expr1...exprN))
     * */
    private static LambdaExprNode convertLambda(SchemeCell lambdaList, Context context) {
        Context lambdaContext = new Context(context, Context.LexicalScope.LAMBDA);

        var params = (SchemeCell) lambdaList.get(1);
        var expressions = (SchemeCell) ((SchemeCell) lambdaList.cdr).cdr;

        List<SchemeExpression> lambdaExprs = new ArrayList<>(createLocalVariablesForLambda(params, lambdaContext));

        for (Object obj : expressions) {
            lambdaExprs.add(ListToExpressionConverter.convert(obj, lambdaContext));
        }

        var frameDescriptor = lambdaContext.getFrameDescriptor();
        SchemeFunction function = SchemeFunction.createFunction(lambdaExprs, params, frameDescriptor);

        return new LambdaExprNode(function);
    }

    private static List<WriteLocalVariableExprNode> createLocalVariablesForLambda(SchemeCell parameters, Context context) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            var currentSymbol = (SchemeSymbol) parameters.get(i);
            int frameIndex = context.addLocalSymbol(currentSymbol);
            var localVariableNode = WriteLocalVariableExprNodeGen.create(new ReadProcedureArgExprNode(i), frameIndex, currentSymbol);
            result.add(localVariableNode);
        }

        return result;
    }

    private static QuoteExprNode convertQuote(SchemeCell quoteList, Context context) {
        if (quoteList.size() == 2) {
            return new QuoteExprNode(quoteList.get(1));
        } else {
            throw new ParserException("quote: arity mismatch \n expected: 1 \n given: " + (quoteList.size() - 1));
        }
    }

    private static QuasiquoteExprNode convertQuasiquote(SchemeCell quasiquoteList, Context context) {
        if (quasiquoteList.size() == 2) {
            return new QuasiquoteExprNode(quasiquoteList.get(1), context);
        } else {
            throw new ParserException("quasiquote: arity mismatch \n expected: 1 \n given: " + (quasiquoteList.size() - 1));
        }
    }

    private static LetExprNode convertLet(SchemeCell letList, Context context) {
        Context letContext = new Context(context, Context.LexicalScope.LET);
        SchemeCell parameters = (SchemeCell) letList.get(1);
        SchemeCell body = (SchemeCell) ((SchemeCell) letList.cdr).cdr;

        List<SchemeExpression> letExpressions = new ArrayList<>(createLocalVariablesForLet(parameters, letContext));

        for (Object obj : body) {
            letExpressions.add(ListToExpressionConverter.convert(obj, letContext));
        }

        var frameDescriptor = letContext.getFrameDescriptor();

        return new LetExprNode(letExpressions, frameDescriptor);

    }

    private static LetStarExprNode convertLetStar(SchemeCell letStarList, Context context) {
        return null;
    }


    private static List<WriteLocalVariableExprNode> createLocalVariablesForLet(SchemeCell parametersList, Context context) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        for (Object obj : parametersList) {
            if (obj instanceof SchemeCell) {
                var currentList = (SchemeCell) obj;
                var symbolExpected = currentList.car;
                if (symbolExpected instanceof SchemeSymbol) {
                    var symbol = (SchemeSymbol) symbolExpected;
                    int frameIndex = context.addLocalSymbol(symbol);
                    var expr = ListToExpressionConverter.convert(currentList.get(1), context);
                    var localVariableNode = WriteLocalVariableExprNodeGen.create(expr, frameIndex, symbol);
                    result.add(localVariableNode);
                    continue;
                }
            }
            throw new ParserException("Parser error int LET: contract violation \n expected: (let ((id val-expr) ...) body ...+)");
        }
        return result;
    }
}
