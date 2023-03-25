package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLetConverter {

    protected static final int CTX_PARAMS_OFFSET = 1;
    protected static final int CTX_BODY_INDEX = 3;


    protected static void validate(SchemeList letList) {
        var localBinding = letList.get(1);
        if (!(localBinding instanceof SchemeList localBindingList)) {
            throw new SchemeException("let: contract violation\nexpected: (let ((<symbol1>> <expr>>) .. (<symbol2> <expr>) ...) <>)", null);
        }

        List<SchemeSymbol> allIdentifiers = new ArrayList<>();
        for (Object obj : localBindingList) {
            if (!(obj instanceof SchemeList list)) {
                throw new SchemeException("let: contract violation\nexpected: (let ((id1 val-expr1) (id2 val-expr2) ...) body ...)", null);
            }

            if (list.size != 2) {
                throw new SchemeException("let: bad syntax\nexpected size of binding is 2\ngiven: " + list.size, null);
            }

            if (!(list.car instanceof SchemeSymbol symbol)) {
                throw new SchemeException("let: contract violation\nexpected: identifier\ngiven: " + list.car, null);
            }

            if (allIdentifiers.contains(symbol)) {
                throw new SchemeException("let: duplicate identifier " + symbol, null);
            } else {
                allIdentifiers.add(symbol);
            }
        }
    }


    protected static ParserRuleContext getParameterExprCtx(@Nullable ParserRuleContext letParamCtx, int index) {
        if (letParamCtx == null) return null;

        var currentParamCtx = letParamCtx.getChild(index + CTX_PARAMS_OFFSET).getChild(0);
        return (ParserRuleContext) currentParamCtx.getChild(2);
    }

    protected static ParserRuleContext getIdentifierCtx(@Nullable ParserRuleContext letParamCtx, int index) {
        if (letParamCtx == null) return null;

        var currentParamCtx = letParamCtx.getChild(index + CTX_PARAMS_OFFSET).getChild(0);
        return (ParserRuleContext) currentParamCtx.getChild(1);
    }
}
