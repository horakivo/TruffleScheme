package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.AndExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

import java.util.List;

public class AndConverter {

    private AndConverter() {}

    public static SchemeExpression convert(SchemeList andList, ParsingContext context) {
        var schemeExprs = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(andList.cdr(), context);
        if (schemeExprs.isEmpty()) return new BooleanLiteralNode(true);
        if (schemeExprs.size() == 1) return OneArgumentExprNodeGen.create(schemeExprs.get(0));
        return reduceAnd(schemeExprs);
    }

    private static AndExprNode reduceAnd(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new AndExprNode(BooleanCastExprNodeGen.create(arguments.remove(0)), reduceAnd(arguments));
        } else {
            return new AndExprNode(BooleanCastExprNodeGen.create(arguments.get(0)), arguments.get(1));
        }
    }
}
