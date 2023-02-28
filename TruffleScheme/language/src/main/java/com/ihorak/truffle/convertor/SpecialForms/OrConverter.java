package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.OrExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class OrConverter {

    private static final int CTX_BODY_INDEX = 2;

    private OrConverter() {
    }

    public static SchemeExpression convert(SchemeList orList, ParsingContext context, ParserRuleContext orCtx) {
        var schemeExprs = TailCallUtil.convertExpressionsToSchemeExpressionsWithTCO(orList.cdr(), context, orCtx, CTX_BODY_INDEX);
        if (schemeExprs.isEmpty())
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(false), orCtx);
        if (schemeExprs.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(OneArgumentExprNodeGen.create(schemeExprs.get(0)), orCtx);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceOr(schemeExprs), orCtx);
    }



    //TODO is it a problem that those doesn't have Source section?
    private static OrExprNode reduceOr(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new OrExprNode(arguments.remove(0), reduceOr(arguments));
        } else {
            return new OrExprNode(arguments.get(0), arguments.get(1));
        }
    }
}
