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

public class CondConverter {

    private static final int COND_START_INDEX = 2;

    private CondConverter() {
    }

    public static SchemeExpression convertCond(SchemeList condList, ParsingContext context, ParserRuleContext condCtx) {
        validate(condList);
        var condExpressions = condList.cdr();
        if (condExpressions.size == 0)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new UndefinedLiteralNode(), condCtx);
        if (condExpressions.size == 1) {
            var condExpr = (SchemeList) condExpressions.get(0);
            var conditionCtx = (ParserRuleContext) condCtx.getChild(3);
            var thenCtx = (ParserRuleContext) condCtx.getChild(4);
            var conditionExpr = InternalRepresentationConverter.convert(condExpr.get(0), context, false, false, conditionCtx);
            var thenExpr = InternalRepresentationConverter.convert(condExpr.get(1), context, true, false, thenCtx);

            var ifExpr = new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(ifExpr, condCtx);
        }

        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceCond(condExpressions, context, condCtx, COND_START_INDEX), condCtx);
    }

    //TODO is it a problem that those doesn't have Source section?
    private static SchemeExpression reduceCond(SchemeList condExpressions, ParsingContext context, ParserRuleContext condCtx, int startCondCtxIndex) {
        if (condExpressions.size > 2) {
            var condExpr = (SchemeList) condExpressions.get(0);
            var currCondCtx = (ParserRuleContext) condCtx.getChild(startCondCtxIndex).getChild(0);

            var conditionCtx = (ParserRuleContext) currCondCtx.getChild(1);
            var conditionExpr = InternalRepresentationConverter.convert(condExpr.get(0), context, false, false, conditionCtx);

            var thenCtx = (ParserRuleContext) currCondCtx.getChild(2);
            var thenExpr = InternalRepresentationConverter.convert(condExpr.get(1), context, true, false, thenCtx);

            return new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, reduceCond(condExpressions.cdr(), context, condCtx, startCondCtxIndex + 1));
        } else {
            return convertCondWithTwoConditions(condExpressions, context, condCtx, startCondCtxIndex);
        }
    }

    private static SchemeExpression convertCondWithTwoConditions(SchemeList condExpressions, ParsingContext context, ParserRuleContext condCtx, int startCondCtxIndex) {
        var firstCondCtx = (ParserRuleContext) condCtx.getChild(startCondCtxIndex).getChild(0);
        var firstCondIR = (SchemeList) condExpressions.get(0);
        var secondCondCtx = (ParserRuleContext) condCtx.getChild(startCondCtxIndex + 1).getChild(0);
        var secondCondIR = (SchemeList) condExpressions.get(1);

        var firstConditionCtx = (ParserRuleContext) firstCondCtx.getChild(1);
        var firstConditionExpr = InternalRepresentationConverter.convert(firstCondIR.get(0), context, false, false, firstConditionCtx);

        var firstThenCtx =  (ParserRuleContext) firstCondCtx.getChild(2);
        var firstThenExpr = InternalRepresentationConverter.convert(firstCondIR.get(1), context, true, false, firstThenCtx);

        var secondThenCtx = (ParserRuleContext) secondCondCtx.getChild(2);
        var secondThenExpr = InternalRepresentationConverter.convert(secondCondIR.get(1), context, true, false, secondThenCtx);

        if (secondCondIR.get(0).equals(new SchemeSymbol("else"))) {
            return new IfElseExprNode(BooleanCastExprNodeGen.create(firstConditionExpr), firstThenExpr, secondThenExpr);
        } else {
            var secondConditionCtx = (ParserRuleContext) secondCondCtx.getChild(1);
            var secondConditionExpr = InternalRepresentationConverter.convert(secondCondIR.get(0), context, false, false, secondConditionCtx);
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
