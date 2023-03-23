package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
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
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class CondConverter {

    private static final int COND_START_INDEX = 2;

    private CondConverter() {
    }

    public static SchemeExpression convertCond(SchemeList condList, boolean isTailCallPosition, ParsingContext context, @Nullable ParserRuleContext condCtx) {
        validate(condList);
        var condExpressions = condList.cdr();
        if (condExpressions.size == 0)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new UndefinedLiteralNode(), condCtx);
        if (condExpressions.size == 1) {
            var condIR = (SchemeList) condExpressions.get(0);
            var currCondCtx = condCtx != null ? (ParserRuleContext) condCtx.getChild(2).getChild(0) : null;

            var conditionCtx = getConditionCtx(currCondCtx);
            var conditionExpr = InternalRepresentationConverter.convert(condIR.get(0), context, false, false, conditionCtx);

            var thenCtx = getThenCtx(currCondCtx);
            var thenExpr = InternalRepresentationConverter.convert(condIR.get(1), context, isTailCallPosition, false, thenCtx);

            var ifExpr = new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(ifExpr, condCtx);
        }

        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceCond(condExpressions, isTailCallPosition, context, condCtx, COND_START_INDEX), condCtx);
    }

    //TODO is it a problem that those doesn't have Source section?
    private static SchemeExpression reduceCond(SchemeList condExpressions, boolean isTailCallPosition, ParsingContext context, @Nullable ParserRuleContext condCtx, int startCondCtxIndex) {
        if (condExpressions.size > 2) {
            var condExpr = (SchemeList) condExpressions.get(0);
            var currCondCtx = condCtx != null ? (ParserRuleContext) condCtx.getChild(startCondCtxIndex).getChild(0) : null;

            var conditionCtx = getConditionCtx(currCondCtx);
            var conditionExpr = InternalRepresentationConverter.convert(condExpr.get(0), context, false, false, conditionCtx);

            var thenCtx = getThenCtx(currCondCtx);
            var thenExpr = InternalRepresentationConverter.convert(condExpr.get(1), context, isTailCallPosition, false, thenCtx);

            return new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, reduceCond(condExpressions.cdr(), isTailCallPosition, context, condCtx, startCondCtxIndex + 1));
        } else {
            return convertCondWithTwoConditions(condExpressions, isTailCallPosition, context, condCtx, startCondCtxIndex);
        }
    }

    private static SchemeExpression convertCondWithTwoConditions(SchemeList condExpressions, boolean isTailCallPosition, ParsingContext context, @Nullable ParserRuleContext condCtx, int startCondCtxIndex) {
        var firstCondCtx = condCtx != null ? (ParserRuleContext) condCtx.getChild(startCondCtxIndex).getChild(0) : null;
        var firstCondIR = (SchemeList) condExpressions.get(0);
        var secondCondCtx = condCtx != null ? (ParserRuleContext) condCtx.getChild(startCondCtxIndex + 1).getChild(0) : null;
        var secondCondIR = (SchemeList) condExpressions.get(1);

        var firstConditionCtx = getConditionCtx(firstCondCtx);
        var firstConditionExpr = InternalRepresentationConverter.convert(firstCondIR.get(0), context, false, false, firstConditionCtx);

        var firstThenCtx = getThenCtx(firstCondCtx);
        var firstThenExpr = InternalRepresentationConverter.convert(firstCondIR.get(1), context, isTailCallPosition, false, firstThenCtx);

        var secondThenCtx = getThenCtx(secondCondCtx);
        var secondThenExpr = InternalRepresentationConverter.convert(secondCondIR.get(1), context, isTailCallPosition, false, secondThenCtx);

        if (secondCondIR.get(0).equals(new SchemeSymbol("else"))) {
            return new IfElseExprNode(BooleanCastExprNodeGen.create(firstConditionExpr), firstThenExpr, secondThenExpr);
        } else {
            var secondConditionCtx = getConditionCtx(secondCondCtx);
            var secondConditionExpr = InternalRepresentationConverter.convert(secondCondIR.get(0), context, false, false, secondConditionCtx);
            return new IfElseExprNode(BooleanCastExprNodeGen.create(firstConditionExpr), firstThenExpr,
                    new IfExprNode(BooleanCastExprNodeGen.create(secondConditionExpr), secondThenExpr));
        }
    }

    private static ParserRuleContext getConditionCtx(@Nullable ParserRuleContext condCtx) {
        if (condCtx == null) return null;
        return (ParserRuleContext) condCtx.getChild(1);
    }

    private static ParserRuleContext getThenCtx(@Nullable ParserRuleContext condCtx) {
        if (condCtx == null) return null;
        return (ParserRuleContext) condCtx.getChild(2);
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
