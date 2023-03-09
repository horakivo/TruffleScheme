package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNode;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.IfExprNode;
import com.ihorak.truffle.parser.R5RSParser;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

public class IfConverter {

    private static final int CTX_CONDITION_INDEX = 2;
    private static final int CTX_THEN_INDEX = 3;
    private static final int CTX_ELSE_INDEX = 4;

    private IfConverter() {
    }

    public static SchemeExpression convert(SchemeList ifList, ParsingContext context, ParserRuleContext ifCtx) {
        validate(ifList);


        if (ifList.size == 3) {
            return covertIfNode(ifList, context, ifCtx);
        } else {
            return covertIfElseNode(ifList, context, ifCtx);
        }
    }

    private static IfExprNode covertIfNode(SchemeList ifList, ParsingContext context, ParserRuleContext ifCtx) {
        var conditionCtx = (ParserRuleContext) ifCtx.children.get(CTX_CONDITION_INDEX);
        var thenCtx = (ParserRuleContext) ifCtx.children.get(CTX_THEN_INDEX);

        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false, conditionCtx);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, true, false, thenCtx);

        var expr = new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
        SourceSectionUtil.setSourceSection(expr, ifCtx);
        return expr;
    }

    private static IfElseExprNode covertIfElseNode(SchemeList ifList, ParsingContext context, ParserRuleContext ifCtx) {
        var conditionCtx = (ParserRuleContext) ifCtx.children.get(CTX_CONDITION_INDEX);
        var thenCtx = (ParserRuleContext) ifCtx.children.get(CTX_THEN_INDEX);
        var elseCtx = (ParserRuleContext) ifCtx.children.get(CTX_ELSE_INDEX);

        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false, conditionCtx);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, true, false, thenCtx);
        var elseExpr = InternalRepresentationConverter.convert(ifList.get(3), context, true, false, elseCtx);

        if (thenExpr instanceof SelfRecursiveTailCallThrowerNode || elseExpr instanceof SelfRecursiveTailCallThrowerNode) {

        }

        var expr =  new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, elseExpr);
        SourceSectionUtil.setSourceSection(expr, ifCtx);
        return expr;
    }


    private static void validate(SchemeList ifList) {
        if (ifList.size == 3 || ifList.size == 4) {
            return;
        }

        throw new SchemeException("if: bad syntax in: " + ifList, null);
    }
}
