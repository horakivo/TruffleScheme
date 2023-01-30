package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.SpecialForms.*;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.*;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpecialFormConverter {

    public static SchemeExpression convertListToSpecialForm(SchemeList specialFormList, ParsingContext context) {
        var operationSymbol = (SchemeSymbol) specialFormList.get(0);
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
                return QuasiquioteConverter.convert(specialFormList, context);
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


    private static QuoteExprNode convertQuote(SchemeList quoteList, ParsingContext context) {
        if (quoteList.size == 2) {
            return new QuoteExprNode(quoteList.get(1));
        } else {
            throw new SchemeException("quote: arity mismatch\nexpected: 1\ngiven: " + (quoteList.size - 1), null);
        }
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
