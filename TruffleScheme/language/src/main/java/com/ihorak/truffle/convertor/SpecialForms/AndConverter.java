package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.AndExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class AndConverter {

    private static final int CTX_BODY_INDEX = 2;

    private AndConverter() {}

    public static SchemeExpression convert(SchemeList andList, ParsingContext context, ParserRuleContext andCtx) {
        var schemeExprs = TailCallUtil.convertExpressionsToSchemeExpressionsWithTCO(andList.cdr(), context, andCtx, CTX_BODY_INDEX);
        if (schemeExprs.isEmpty()) {
            var expr = new BooleanLiteralNode(true);
            SourceSectionUtil.setSourceSection(expr, andCtx);

            return expr;
        }
        if (schemeExprs.size() == 1) {
            var expr = OneArgumentExprNodeGen.create(schemeExprs.get(0));
            SourceSectionUtil.setSourceSection(expr, andCtx);

            return expr;
        }
        var andExpr = reduceAnd(schemeExprs);
        SourceSectionUtil.setSourceSection(andExpr, andCtx);

        return andExpr;
    }

    //TODO is it a problem that those doesn't have Source section?
    private static AndExprNode reduceAnd(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new AndExprNode(BooleanCastExprNodeGen.create(arguments.remove(0)), reduceAnd(arguments));
        } else {
            return new AndExprNode(BooleanCastExprNodeGen.create(arguments.get(0)), arguments.get(1));
        }
    }
}
