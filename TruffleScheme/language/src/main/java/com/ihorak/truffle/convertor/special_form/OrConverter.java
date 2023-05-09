package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.OrExprNode;
import com.ihorak.truffle.node.special_form.OrExprNodeGen;
import com.ihorak.truffle.runtime.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OrConverter extends AndOrAbstractConverter {


    private OrConverter() {
    }

    public static SchemeExpression convert(SchemeList orList, boolean isTailCallPosition, ConverterContext context, @Nullable ParserRuleContext orCtx) {
        List<SchemeExpression> bodyExprs = getBodyExpr(orList.cdr, isTailCallPosition, context, orCtx);
        if (bodyExprs.isEmpty())
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(false), orCtx);
        if (bodyExprs.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(EvalToSelfExprNodeGen.create(bodyExprs.get(0)), orCtx);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceOr(bodyExprs), orCtx);
    }

    private static OrExprNode reduceOr(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return OrExprNodeGen.create(arguments.remove(0), reduceOr(arguments));
        } else {
            return OrExprNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }
}
