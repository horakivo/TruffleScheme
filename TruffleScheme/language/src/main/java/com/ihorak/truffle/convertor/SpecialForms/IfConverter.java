package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.IfExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

public class IfConverter {

    private IfConverter() {}

    public static SchemeExpression convert(SchemeList ifList, ParsingContext context) {
        validate(ifList);

        if (ifList.size == 3) {
            return covertIfNode(ifList, context);
        } else {
            return covertIfElseNode(ifList, context);
        }
    }

    private static IfExprNode covertIfNode(SchemeList ifList, ParsingContext context) {
        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, true, false);

        return new IfExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr);
    }

    private static IfElseExprNode covertIfElseNode(SchemeList ifList, ParsingContext context) {
        var conditionExpr = InternalRepresentationConverter.convert(ifList.get(1), context, false, false);
        var thenExpr = InternalRepresentationConverter.convert(ifList.get(2), context, true, false);
        var elseExpr = InternalRepresentationConverter.convert(ifList.get(3), context, true, false);

        return new IfElseExprNode(BooleanCastExprNodeGen.create(conditionExpr), thenExpr, elseExpr);
    }


    private static void validate(SchemeList ifList) {
        if (ifList.size == 3 || ifList.size == 4) {
            return;
        }

        throw new SchemeException("if: bad syntax in: " + ifList, null);
    }
}
