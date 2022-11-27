package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.SpecialForms.*;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
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
                return IfConverter.convert(specialFormList, context);
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
            case "letrec":
                return LetrecConverter.convert(specialFormList, context);
            case "and":
                return AndConverter.convert(specialFormList, context);
            case "or":
                return OrConverter.convert(specialFormList, context);
            case "cond":
                return CondConverter.convertCond(specialFormList, context);
            default:
                throw new IllegalArgumentException("Unknown special form");

        }
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
                    result.add(InternalRepresentationConverter.convert(list.get(1), context, false));
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


//    private static List<WriteLocalVariableExprNode> createLocalVariablesForLetStar(SchemeCell parametersList, ParsingContext context) {
//        List<WriteLocalVariableExprNode> result = new ArrayList<>();
//        for (Object obj : parametersList) {
//            if (obj instanceof SchemeCell) {
//                var currentList = (SchemeCell) obj;
//                if (currentList.size() != 2) {
//                    throw new SchemeException("let: bad syntax (not an identifier and expression for a binding)", null);
//                }
//                var symbolExpected = currentList.car;
//                if (symbolExpected instanceof SchemeSymbol) {
//                    var valueToStore = ListToExpressionConverter.convert(currentList.get(1), context);
//                    var symbol = (SchemeSymbol) symbolExpected;
//                    int frameIndex = context.findOrAddLocalSymbol(symbol);
//                    var localVariableNode = WriteLocalVariableExprNodeGen.create(frameIndex, symbol, valueToStore);
//                    result.add(localVariableNode);
//                    continue;
//                }
//            }
//            throw new SchemeException("Parser error in LET: contract violation \n expected: (let ((id val-expr) ...) body ...+)", null);
//        }
//        return result;
//    }



}
