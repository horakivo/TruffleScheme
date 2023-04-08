package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.OrExprNode;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OrConverter extends AndOrAbstractConverter{


    private OrConverter() {
    }

    public static SchemeExpression convert(SchemeList orList, boolean isTailCallPosition, ParsingContext context, @Nullable ParserRuleContext orCtx) {
        List<SchemeExpression> bodyExprs = getBodyExpr(orList.cdr, isTailCallPosition, context, orCtx);
        if (bodyExprs.isEmpty())
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(false), orCtx);
        if (bodyExprs.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(OneArgumentExprNodeGen.create(bodyExprs.get(0)), orCtx);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceOr(bodyExprs), orCtx);
    }

    private static OrExprNode reduceOr(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new OrExprNode(arguments.remove(0), reduceOr(arguments));
        } else {
            return new OrExprNode(arguments.get(0), arguments.get(1));
        }
    }
}
