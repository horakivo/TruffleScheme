package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.OneArgumentExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.special_form.OrExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

import java.util.List;

public class OrConverter {

    private OrConverter() {}

    public static SchemeExpression convert(SchemeList orList, ParsingContext context) {
        var schemeExprs = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(orList.cdr(), context);
        if (schemeExprs.isEmpty()) return new BooleanLiteralNode(false);
        if (schemeExprs.size() == 1) return OneArgumentExprNodeGen.create(schemeExprs.get(0));
        return reduceOr(schemeExprs);
    }



    private static OrExprNode reduceOr(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new OrExprNode(arguments.remove(0), reduceOr(arguments));
        } else {
            return new OrExprNode(arguments.get(0), arguments.get(1));
        }
    }
}
