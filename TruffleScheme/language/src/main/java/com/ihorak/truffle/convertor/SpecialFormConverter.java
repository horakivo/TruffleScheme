package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.ProcedureRootNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.UndefinedLiteralNode;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.scope.WriteGlobalVariableExprNodeGen;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.node.special_form.*;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.node.special_form.LetStarExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.type.*;

import java.util.ArrayList;
import java.util.List;

public class SpecialFormConverter {

    public static SchemeExpression convertListToSpecialForm(SchemeCell specialFormList, ParsingContext context) {
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
            case "and":
                return convertAnd(specialFormList, context);
            case "or":
                return convertOr(specialFormList, context);
            case "cond":
                return convertCond(specialFormList, context);
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
    private static IfExprNode convertIf(SchemeCell ifList, ParsingContext context) {
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
    private static SchemeExpression convertDefine(SchemeCell defineList, ParsingContext context) {
        var potentialSymbol = defineList.get(1);

        if (potentialSymbol instanceof SchemeSymbol) {
            var symbol = (SchemeSymbol) potentialSymbol;
            var defineBody = defineList.get(2);

            if (context.getLexicalScope() == LexicalScope.GLOBAL) {
                return createWriteGlobalVariable(context, symbol, defineBody);
            }

            return createWriteLocalVariable(context, symbol, defineBody);
        } else {
            throw new ParserException("define: expected: Symbol \n given: " + potentialSymbol);
        }
    }

    private static SchemeExpression createWriteGlobalVariable(ParsingContext context, SchemeSymbol symbol, Object defineBody) {
        SchemeExpression valueToStore = ListToExpressionConverter.convert(defineBody, context);
        return WriteGlobalVariableExprNodeGen.create(symbol, valueToStore);
    }

    private static SchemeExpression createWriteLocalVariable(ParsingContext context, SchemeSymbol symbol, Object defineBody) {
        var index = context.findLocalSymbol(symbol);
        if (index == null) {
            index = context.addLocalSymbol(symbol);
        }
        SchemeExpression valueToStore = ListToExpressionConverter.convert(defineBody, context);
        return WriteLocalVariableExprNodeGen.create(index, symbol, valueToStore);
    }

    /*
     *  --> (lambda (param1 .. paramN) expr1...exprN))
     * */
    private static LambdaExprNode convertLambda(SchemeCell lambdaList, ParsingContext context) {
        ParsingContext lambdaContext = new ParsingContext(context, LexicalScope.LAMBDA, context.getLanguage(), context.getMode());

        var params = lambdaList.get(1);
        var expressions = lambdaList.cdr.cdr;

        List<SchemeExpression> paramExprs = createLocalVariablesForLambda(params, lambdaContext);
        List<SchemeExpression> bodyExprs = createLambdaBody(expressions, lambdaContext);

        List<SchemeExpression> allLambdaExpressions = new ArrayList<>();
        allLambdaExpressions.addAll(paramExprs);
        allLambdaExpressions.addAll(bodyExprs);
        var frameDescriptor = lambdaContext.getFrameDescriptor();
        var rootNode = new ProcedureRootNode(context.getLanguage(), frameDescriptor, allLambdaExpressions);
        var hasOptionalArgs = params instanceof SchemePair;
        var function = new UserDefinedProcedure(rootNode.getCallTarget(), paramExprs.size(), hasOptionalArgs);
        return new LambdaExprNode(function);
    }

    private static List<SchemeExpression> createLambdaBody(SchemeCell expressions, ParsingContext lambdaContext) {
        List<SchemeExpression> bodyExprs = new ArrayList<>();
        for (Object obj : expressions) {
            bodyExprs.add(ListToExpressionConverter.convert(obj, lambdaContext));
        }

        bodyExprs.get(bodyExprs.size() - 1).setTailRecursiveAsTrue();

        return bodyExprs;
    }

    private static List<SchemeExpression> createLocalVariablesForLambda(Object parameters, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        if (parameters instanceof SchemeCell) {
            var params = (SchemeCell) parameters;
            for (int i = 0; i < params.size(); i++) {
                var writeLocalVariable = createWriteLocalVariableExprNode(params.get(i), context, i);
                result.add(writeLocalVariable);
            }
        } else if (parameters instanceof SchemePair) {
            int index = 0;
            var params = (SchemePair) parameters;
            while (params.getSecond() instanceof SchemePair) {
                var writeLocalVariable = createWriteLocalVariableExprNode(params.getFirst(), context, index);
                result.add(writeLocalVariable);
                index++;
                params = (SchemePair) params.getSecond();
            }
            result.add(createWriteLocalVariableExprNode(params.getFirst(), context, index));
            index++;
            result.add(createWriteLocalVariableExprNode(params.getSecond(), context, index));
        }

        return result;
    }

    private static WriteLocalVariableExprNode createWriteLocalVariableExprNode(Object symbol, ParsingContext context, int index) {
        var currentSymbol = (SchemeSymbol) symbol;
        int frameIndex = context.addLocalSymbol(currentSymbol);
        return WriteLocalVariableExprNodeGen.create(frameIndex, currentSymbol, new ReadProcedureArgExprNode(index));
    }


    private static QuoteExprNode convertQuote(SchemeCell quoteList, ParsingContext context) {
        if (quoteList.size() == 2) {
            return new QuoteExprNode(quoteList.get(1));
        } else {
            throw new SchemeException("quote: arity mismatch\nexpected: 1\ngiven: " + (quoteList.size() - 1), null);
        }
    }

    private static QuasiquoteExprNode convertQuasiquote(SchemeCell quasiquoteList, ParsingContext context) {
        if (quasiquoteList.size() == 2) {
            return new QuasiquoteExprNode(quasiquoteList.get(1), context);
        } else {
            throw new SchemeException("quasiquote: arity mismatch\nexpected: 1\ngiven: " + (quasiquoteList.size() - 1), null);
        }
    }

    private static LetExprNode convertLet(SchemeCell letList, ParsingContext context) {
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LET, context.getLanguage(), context.getMode());
        SchemeCell parameters = (SchemeCell) letList.get(1);
        SchemeCell body = letList.cdr.cdr;

        List<SchemeExpression> paramValues = getParamsValuesForLet(parameters, context);
        List<SchemeExpression> letExpressions = new ArrayList<>(createLocalVariablesForLet(parameters, letContext));

        for (Object obj : body) {
            letExpressions.add(ListToExpressionConverter.convert(obj, letContext));
        }

        var frameDescriptor = letContext.getFrameDescriptor();

        return new LetExprNode(letExpressions, paramValues, frameDescriptor);

    }

    private static SchemeExpression convertLetStar(SchemeCell letStarList, ParsingContext context) {
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LET, context.getLanguage(), context.getMode());
        SchemeCell parameters = (SchemeCell) letStarList.get(1);
        SchemeCell body = letStarList.cdr.cdr;

        List<SchemeExpression> letExpressions = new ArrayList<>(createLocalVariablesForLetStar(parameters, letContext));

        for (Object obj : body) {
            letExpressions.add(ListToExpressionConverter.convert(obj, letContext));
        }

        var frameDescriptor = letContext.getFrameDescriptor();

        return new LetStarExprNode(letExpressions, frameDescriptor);
    }


    private static List<WriteLocalVariableExprNode> createLocalVariablesForLet(SchemeCell parametersList, ParsingContext context) {
        List<WriteLocalVariableExprNode> writeParams = new ArrayList<>();
        int index = 0;
        for (Object obj : parametersList) {
            if (obj instanceof SchemeCell) {
                var currentList = (SchemeCell) obj;
                var symbolExpected = currentList.car;
                if (symbolExpected instanceof SchemeSymbol) {
                    var symbol = (SchemeSymbol) symbolExpected;
                    int frameIndex = context.addLocalSymbol(symbol);
                    var valueToStore = new ReadProcedureArgExprNode(index);
                    var localVariableNode = WriteLocalVariableExprNodeGen.create(frameIndex, symbol, valueToStore);
                    writeParams.add(localVariableNode);
                    index++;
                    continue;
                }
            }
            throw new SchemeException("Parser error in LET: contract violation \n expected: (let ((id val-expr) ...) body ...+)", null);
        }
        return writeParams;
    }

    private static List<SchemeExpression> getParamsValuesForLet(SchemeCell parametersList, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        for (Object obj : parametersList) {
            var currentList = (SchemeCell) obj;
            if (currentList.size() != 2) {
                throw new SchemeException("let: bad syntax (not an identifier and expression for a binding)", null);
            }
            result.add(ListToExpressionConverter.convert(currentList.get(1), context));
        }

        return result;
    }


    private static List<WriteLocalVariableExprNode> createLocalVariablesForLetStar(SchemeCell parametersList, ParsingContext context) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        for (Object obj : parametersList) {
            if (obj instanceof SchemeCell) {
                var currentList = (SchemeCell) obj;
                if (currentList.size() != 2) {
                    throw new SchemeException("let: bad syntax (not an identifier and expression for a binding)", null);
                }
                var symbolExpected = currentList.car;
                if (symbolExpected instanceof SchemeSymbol) {
                    var valueToStore = ListToExpressionConverter.convert(currentList.get(1), context);
                    var symbol = (SchemeSymbol) symbolExpected;
                    int frameIndex = context.addLocalSymbol(symbol);
                    var localVariableNode = WriteLocalVariableExprNodeGen.create(frameIndex, symbol, valueToStore);
                    result.add(localVariableNode);
                    continue;
                }
            }
            throw new SchemeException("Parser error in LET: contract violation \n expected: (let ((id val-expr) ...) body ...+)", null);
        }
        return result;
    }


    private static SchemeExpression convertAnd(SchemeCell andList, ParsingContext context) {
        var schemeExprs = convertSchemeCellToSchemeExpressions(andList.cdr, context);
        if (schemeExprs.size() == 0) return new BooleanLiteralNode(true);
        if (schemeExprs.size() == 1) return OneArgumentExprNodeGen.create(schemeExprs.get(0));
        return reduceAnd(schemeExprs);
    }

    private static AndExprNode reduceAnd(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new AndExprNode(arguments.remove(0), reduceAnd(arguments));
        } else {
            return new AndExprNode(arguments.get(0), arguments.get(1));
        }
    }

    private static SchemeExpression convertOr(SchemeCell orList, ParsingContext context) {
        var schemeExprs = convertSchemeCellToSchemeExpressions(orList.cdr, context);
        if (schemeExprs.size() == 0) return new BooleanLiteralNode(false);
        if (schemeExprs.size() == 1) return OneArgumentExprNodeGen.create(schemeExprs.get(0));
        return reduceOr(schemeExprs);
    }

    private static SchemeExpression convertCond(SchemeCell condList, ParsingContext context) {
        var conditions = condList.cdr;
        if (conditions.size() == 0) return new UndefinedLiteralNode();
        if (conditions.size() == 1) {
            if (conditions.car instanceof SchemeCell) {
                var condition = (SchemeCell) conditions.car;
                var conditionExpr = ListToExpressionConverter.convert(condition.get(0), context);
                var thenExpr = ListToExpressionConverter.convert(condition.get(1), context);
                return new IfExprNode(conditionExpr, thenExpr, null);
            } else {
                throw new SchemeException("cond: bad syntax\nexpected: list?\ngiven: " + conditions.car, null);
            }
        }

        return reduceCond(conditions, context);
    }

    private static IfExprNode reduceCond(SchemeCell conditionsList, ParsingContext context) {
        if (conditionsList.size() > 2) {
            if (!(conditionsList.car instanceof SchemeCell))
                throw new SchemeException("cond: bad syntax\nexpected: list?\ngiven: " + conditionsList.car, null);
            var firstConditionList = (SchemeCell) conditionsList.car;
            var conditionExpr = ListToExpressionConverter.convert(firstConditionList.get(0), context);
            var thenExpr = ListToExpressionConverter.convert(firstConditionList.get(1), context);
            return new IfExprNode(conditionExpr, thenExpr, reduceCond(conditionsList.cdr, context));

        } else {
            return convertCondWithTwoConditions(conditionsList, context);
        }
    }

    private static IfExprNode convertCondWithTwoConditions(SchemeCell conditionsList, ParsingContext context) {
        if (!(conditionsList.car instanceof SchemeCell))
            throw new SchemeException("cond: bad syntax\nexpected: list?\ngiven: " + conditionsList.car, null);
        if (!(conditionsList.get(1) instanceof SchemeCell))
            throw new SchemeException("cond: bad syntax\nexpected: list?\ngiven: " + conditionsList.get(1), null);

        var firstConditionList = (SchemeCell) conditionsList.car;
        var secondConditionList = (SchemeCell) conditionsList.get(1);

        var firstConditionExpr = ListToExpressionConverter.convert(firstConditionList.get(0), context);
        var firstThenExpr = ListToExpressionConverter.convert(firstConditionList.get(1), context);

        if (secondConditionList.get(0).equals(new SchemeSymbol("else"))) {
            var elseExpr = ListToExpressionConverter.convert(secondConditionList.get(1), context);
            return new IfExprNode(firstConditionExpr, firstThenExpr, elseExpr);
        } else {
            var secondConditionExpr = ListToExpressionConverter.convert(secondConditionList.get(0), context);
            var secondThenExpr = ListToExpressionConverter.convert(secondConditionList.get(1), context);
            return new IfExprNode(firstConditionExpr, firstThenExpr, new IfExprNode(secondConditionExpr, secondThenExpr, null));
        }
    }


    private static OrExprNode reduceOr(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new OrExprNode(arguments.remove(0), reduceOr(arguments));
        } else {
            return new OrExprNode(arguments.get(0), arguments.get(1));
        }
    }

    private static List<SchemeExpression> convertSchemeCellToSchemeExpressions(SchemeCell schemeCell, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        for (Object obj : schemeCell) {
            result.add(ListToExpressionConverter.convert(obj, context));
        }

        return result;
    }

}
