package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNode;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.scope.StoreAndReturnValueExprNode;
import com.ihorak.truffle.node.scope.StoreAndReturnValueExprNodeGen;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.IfExprNode;
import com.ihorak.truffle.parser.R5RSParser;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.jetbrains.annotations.Nullable;

public class IfConverter {

    private static final int CTX_CONDITION_INDEX = 2;
    private static final int CTX_THEN_INDEX = 3;
    private static final int CTX_ELSE_INDEX = 4;

    private IfConverter() {
    }

    public static SchemeExpression convert(SchemeList ifList, boolean isTailCallPosition, ParsingContext context, ParserRuleContext ifCtx) {
        validate(ifList);


        if (ifList.size == 3) {
            return covertIfNode(ifList, isTailCallPosition, context, ifCtx);
        } else {
            return covertIfElseNode(ifList, isTailCallPosition, context, ifCtx);
        }
    }

    private static IfExprNode covertIfNode(SchemeList ifList, boolean isTailCallPosition, ParsingContext context, ParserRuleContext ifCtx) {
        var conditionCtx = getConditionCtxOrNull(ifCtx);
        var thenCtx = getThenCtxOrNull(ifCtx);

        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false, conditionCtx);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, isTailCallPosition, false, thenCtx);

        var expr = new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
        SourceSectionUtil.setSourceSection(expr, ifCtx);
        return expr;
    }

    private static IfElseExprNode covertIfElseNode(SchemeList ifList, boolean isTailCallPosition, ParsingContext context, ParserRuleContext ifCtx) {
        var conditionCtx = getConditionCtxOrNull(ifCtx);
        var thenCtx = getThenCtxOrNull(ifCtx);
        var elseCtx = getElseCtxOrNull(ifCtx);

        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false, conditionCtx);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, isTailCallPosition, false, thenCtx);
        var elseExpr = InternalRepresentationConverter.convert(ifList.get(3), context, isTailCallPosition, false, elseCtx);

//        //TODO we need to do that recursively (of thenExpr or elseExpr is If then again)
//        if (thenExpr instanceof SelfRecursiveTailCallThrowerNode || elseExpr instanceof SelfRecursiveTailCallThrowerNode) {
//            var bothSelfTCO = thenExpr instanceof SelfRecursiveTailCallThrowerNode && elseExpr instanceof SelfRecursiveTailCallThrowerNode;
//            if (!bothSelfTCO) {
//                int resultIndex = context.getSelfTailRecursionResultIndex().orElseThrow(InterpreterException::shouldNotReachHere);
//                if (thenExpr instanceof SelfRecursiveTailCallThrowerNode) {
//                    elseExpr = StoreAndReturnValueExprNodeGen.create(resultIndex, elseExpr);
//                }
//                if (elseExpr instanceof SelfRecursiveTailCallThrowerNode) {
//                    thenExpr = StoreAndReturnValueExprNodeGen.create(resultIndex, thenExpr);
//                }
//            }
//        }

        var expr = new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, elseExpr);
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
