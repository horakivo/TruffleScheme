package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.AndExprNode;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AndConverter extends AndOrAbstractConverter {


    private AndConverter() {
    }

    public static SchemeExpression convert(SchemeList andList, boolean isTailCallPosition, ParsingContext context, @Nullable ParserRuleContext andCtx) {
        List<SchemeExpression> bodyExprs = getBodyExpr(andList.cdr, isTailCallPosition, context, andCtx);

        if (bodyExprs.isEmpty()) {
            var expr = new BooleanLiteralNode(true);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, andCtx);
        }
        if (bodyExprs.size() == 1) {
            var expr = OneArgumentExprNodeGen.create(bodyExprs.get(0));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, andCtx);
        }
        var andExpr = reduceAnd(bodyExprs);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(andExpr, andCtx);
    }

    private static AndExprNode reduceAnd(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new AndExprNode(BooleanCastExprNodeGen.create(arguments.remove(0)), reduceAnd(arguments));
        } else {
            return new AndExprNode(BooleanCastExprNodeGen.create(arguments.get(0)), arguments.get(1));
        }
    }
}
