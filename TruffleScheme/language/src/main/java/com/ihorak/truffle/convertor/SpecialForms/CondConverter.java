package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.literals.UndefinedLiteralNode;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.IfExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;

public class CondConverter {

    private CondConverter() {}

    public static SchemeExpression convertCond(SchemeList condList, ParsingContext context) {
        validate(condList);
        var condExpressions = condList.cdr();
        if (condExpressions.size == 0) return new UndefinedLiteralNode();
        if (condExpressions.size == 1) {
            var condExpr = (SchemeList) condExpressions.get(0);
            var conditionExpr = InternalRepresentationConverter.convert(condExpr.get(0), context, false, false);
            var thenExpr = InternalRepresentationConverter.convert(condExpr.get(1), context, true, false);
            return new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
        }

        return reduceCond(condExpressions, context);
    }

    private static SchemeExpression reduceCond(SchemeList condExpressions, ParsingContext context) {
        if (condExpressions.size > 2) {
            var firstCondExpr = (SchemeList) condExpressions.get(0);
            var conditionExpr = InternalRepresentationConverter.convert(firstCondExpr.get(0), context, false, false);
            var thenExpr = InternalRepresentationConverter.convert(firstCondExpr.get(1), context, true, false);
            return new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, reduceCond(condExpressions.cdr(), context));
        } else {
            return convertCondWithTwoConditions(condExpressions, context);
        }
    }

    private static SchemeExpression convertCondWithTwoConditions(SchemeList condExpressions, ParsingContext context) {
        var firstCondExpr = (SchemeList) condExpressions.get(0);
        var secondCondExpr = (SchemeList) condExpressions.get(1);

        var firstConditionExpr = InternalRepresentationConverter.convert(firstCondExpr.get(0), context, false, false);
        var firstThenExpr = InternalRepresentationConverter.convert(firstCondExpr.get(1), context, true, false);
        var secondThenExpr = InternalRepresentationConverter.convert(secondCondExpr.get(1), context, true, false);

        if (secondCondExpr.get(0).equals(new SchemeSymbol("else"))) {
            var elseExpr = InternalRepresentationConverter.convert(secondCondExpr.get(1), context, true, false);
            return new IfElseExprNode(BooleanCastExprNodeGen.create(firstConditionExpr), firstThenExpr, elseExpr);
        } else {
            var secondConditionExpr = InternalRepresentationConverter.convert(secondCondExpr.get(0), context, false, false);
            return new IfElseExprNode(BooleanCastExprNodeGen.create(firstConditionExpr), firstThenExpr,
                                      new IfExprNode(BooleanCastExprNodeGen.create(secondConditionExpr), secondThenExpr));
        }
    }


    private static void validate(SchemeList condList) {
        var condsExpression = condList.cdr();
        for (Object obj : condsExpression) {
            if (!(obj instanceof SchemeList list)) {
                throw new SchemeException("cond: bad syntax\nexpected: list?\ngiven: " + obj, null);
            }

            if (list.size != 2) {
                throw new SchemeException("""
                                                  cond: bad syntax
                                                  expected syntax: (cond (<test1> <action1>)
                                                                                .
                                                                                .
                                                                                .
                                                                         (<testN> <actionN>)
                                                                         (else <action>))        
                                                  """, null);
            }
        }
    }
}
