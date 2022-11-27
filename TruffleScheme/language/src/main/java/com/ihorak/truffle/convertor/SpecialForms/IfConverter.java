package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.IfExprNode;
import com.ihorak.truffle.type.SchemeCell;

public class IfConverter {

    private IfConverter() {}

    public static SchemeExpression convert(SchemeCell ifList, ParsingContext context) {
        validate(ifList);

        if (ifList.size() == 3) {
            return covertIfNode(ifList, context);
        } else {
            return covertIfElseNode(ifList, context);
        }
    }

    private static IfExprNode covertIfNode(SchemeCell ifList, ParsingContext context) {
        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, true);

        return new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
    }

    private static IfElseExprNode covertIfElseNode(SchemeCell ifList, ParsingContext context) {
        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, true);
        var elseExpr = InternalRepresentationConverter.convert(ifList.get(3), context, true);

        return new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, elseExpr);
    }


    private static void validate(SchemeCell ifList) {
        if (ifList.size() == 3 || ifList.size() == 4) {
            return;
        }

        throw new SchemeException("if: bad syntax in: " + ifList, null);
    }
}
