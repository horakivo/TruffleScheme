package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.IfElseExprNodeGen;
import com.ihorak.truffle.node.special_form.IfExprNode;
import com.ihorak.truffle.node.special_form.IfExprNodeGen;
import com.ihorak.truffle.runtime.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class IfConverter {

    private static final int CTX_CONDITION_INDEX = 2;
    private static final int CTX_THEN_INDEX = 3;
    private static final int CTX_ELSE_INDEX = 4;

    private IfConverter() {
    }

    public static SchemeExpression convert(SchemeList ifList, boolean isTailCallPosition, ConverterContext context, ParserRuleContext ifCtx) {
        validate(ifList);


        if (ifList.size == 3) {
            return covertIfNode(ifList, isTailCallPosition, context, ifCtx);
        } else {
            return covertIfElseNode(ifList, isTailCallPosition, context, ifCtx);
        }
    }

    private static IfExprNode covertIfNode(SchemeList ifList, boolean isTailCallPosition, ConverterContext context, ParserRuleContext ifCtx) {
        var conditionCtx = getConditionCtxOrNull(ifCtx);
        var thenCtx = getThenCtxOrNull(ifCtx);

        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false, conditionCtx);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, isTailCallPosition, false, thenCtx);

        var expr = IfExprNodeGen.create(conditionExpr, thenExpr);
        SourceSectionUtil.setSourceSection(expr, ifCtx);
        return expr;
    }

    private static IfElseExprNode covertIfElseNode(SchemeList ifList, boolean isTailCallPosition, ConverterContext context, ParserRuleContext ifCtx) {
        var conditionCtx = getConditionCtxOrNull(ifCtx);
        var thenCtx = getThenCtxOrNull(ifCtx);
        var elseCtx = getElseCtxOrNull(ifCtx);

        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false, conditionCtx);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, isTailCallPosition, false, thenCtx);
        var elseExpr = InternalRepresentationConverter.convert(ifList.get(3), context, isTailCallPosition, false, elseCtx);

        var expr = IfElseExprNodeGen.create(conditionExpr, thenExpr, elseExpr);
        SourceSectionUtil.setSourceSection(expr, ifCtx);
        return expr;
    }

    private static ParserRuleContext getConditionCtxOrNull(@Nullable ParserRuleContext ifCtx) {
        if (ifCtx == null) return null;
        return (ParserRuleContext) ifCtx.getChild(CTX_CONDITION_INDEX);
    }

    private static ParserRuleContext getThenCtxOrNull(@Nullable ParserRuleContext ifCtx) {
        if (ifCtx == null) return null;
        return (ParserRuleContext) ifCtx.getChild(CTX_THEN_INDEX);
    }

    private static ParserRuleContext getElseCtxOrNull(@Nullable ParserRuleContext ifCtx) {
        if (ifCtx == null) return null;
        return (ParserRuleContext) ifCtx.getChild(CTX_ELSE_INDEX);
    }


    private static void validate(SchemeList ifList) {
        if (ifList.size == 3 || ifList.size == 4) {
            return;
        }

        throw new SchemeException("if: bad syntax in: " + ifList, null);
    }
}
