package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.AndExprNode;
import com.ihorak.truffle.node.special_form.AndExprNodeGen;
import com.ihorak.truffle.runtime.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AndConverter extends AndOrAbstractConverter {


    private AndConverter() {
    }

    public static SchemeExpression convert(SchemeList andList, boolean isTailCallPosition, ConverterContext context, @Nullable ParserRuleContext andCtx) {
        List<SchemeExpression> bodyExprs = getBodyExpr(andList.cdr, isTailCallPosition, context, andCtx);

        if (bodyExprs.isEmpty()) {
            var expr = new BooleanLiteralNode(true);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, andCtx);
        }
        if (bodyExprs.size() == 1) {
            var expr = EvalToSelfExprNodeGen.create(bodyExprs.get(0));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, andCtx);
        }
        var andExpr = reduceAnd(bodyExprs);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(andExpr, andCtx);
    }

    private static AndExprNode reduceAnd(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return AndExprNodeGen.create(arguments.remove(0), reduceAnd(arguments));
        } else {
            return AndExprNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }
}
