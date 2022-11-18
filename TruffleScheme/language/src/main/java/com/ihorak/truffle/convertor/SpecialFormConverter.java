package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.SpecialForms.DefineConverter;
import com.ihorak.truffle.convertor.SpecialForms.LambdaConverter;
import com.ihorak.truffle.convertor.SpecialForms.LetConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.UndefinedLiteralNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.node.special_form.*;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpecialFormConverter {

    public static SchemeExpression convertListToSpecialForm(SchemeCell specialFormList, ParsingContext context) {
        var operationSymbol = (SchemeSymbol) specialFormList.car;
        switch (operationSymbol.getValue()) {
            case "if":
                return convertIf(specialFormList, context);
            case "define":
                return DefineConverter.convert(specialFormList, context);
            case "lambda":
                return LambdaConverter.convert(specialFormList, context);
            case "quote":
                return convertQuote(specialFormList, context);
            case "quasiquote":
                return convertQuasiquote(specialFormList, context);
            case "let":
                return LetConverter.convert(specialFormList, context);
//            case "let*":
//                return convertLetStar(specialFormList, context);
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


    private static QuoteExprNode convertQuote(SchemeCell quoteList, ParsingContext context) {
        if (quoteList.size() == 2) {
            return new QuoteExprNode(quoteList.get(1));
        } else {
            throw new SchemeException("quote: arity mismatch\nexpected: 1\ngiven: " + (quoteList.size() - 1), null);
        }
    }

    private static QuasiquoteExprNode convertQuasiquote(SchemeCell quasiquoteList, ParsingContext context) {
        if (quasiquoteList.size() == 2) {
            var toBeEvalExpr = quasiquoteHelper(quasiquoteList.get(1), context);
            Collections.reverse(toBeEvalExpr);
            return new QuasiquoteExprNode(quasiquoteList.get(1), toBeEvalExpr, context);
        } else {
            throw new SchemeException("quasiquote: arity mismatch\nexpected: 1\ngiven: " + (quasiquoteList.size() - 1), null);
        }
    }

    private static List<SchemeExpression> quasiquoteHelper(Object datum, ParsingContext context) {
        if (datum instanceof SchemeCell list) {
            return convertList(list, context);
//            for (Object element : list) {
//                if (element instanceof SchemeCell sublist && isUnquoteOrUnquoteSplicingList(sublist)) {
//                    if (sublist.size() == 2) {
//                        result.add(ListToExpressionConverter.convert(sublist.get(1), context));
//                    } else {
//                        throw new SchemeException("unquote: expects exactly one expression", null);
//                    }
//                }
//            }
        }

        return new ArrayList<>();
    }

    private static List<SchemeExpression> convertList(SchemeCell schemeCell, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        for (Object element : schemeCell) {
            if (element instanceof SchemeCell list) {
                if (isUnquoteOrUnquoteSplicingList(list)) {
                    if (list.size() != 2) throw new SchemeException("unquote: expects exactly one expression", null);
                    result.add(ListToExpressionConverter.convert(list.get(1), context));
                } else {
                    result.addAll(convertList(list, context));
                }
            }
        }

        return result;
    }

    private static boolean isUnquoteOrUnquoteSplicingList(SchemeCell list) {
        var firstElement = list.car;
        if (firstElement instanceof SchemeSymbol symbol) {
            return symbol.getValue().equals("unquote") || symbol.getValue().equals("unquote-splicing");
        }

        return false;
    }

//    private static SchemeExpression convertLetStar(SchemeCell letStarList, ParsingContext context) {
//        ParsingContext letContext = new ParsingContext(context, LexicalScope.LET, context.getLanguage(), context.getMode());
//        SchemeCell parameters = (SchemeCell) letStarList.get(1);
//        SchemeCell body = letStarList.cdr.cdr;
//
//        List<SchemeExpression> letExpressions = new ArrayList<>(createLocalVariablesForLetStar(parameters, letContext));
//
//        for (Object obj : body) {
//            letExpressions.add(ListToExpressionConverter.convert(obj, letContext));
//        }
//
//        var frameDescriptor = letContext.buildAndGetFrameDescriptor();
//
//        return new LetStarExprNode(letExpressions, frameDescriptor);
//    }


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
