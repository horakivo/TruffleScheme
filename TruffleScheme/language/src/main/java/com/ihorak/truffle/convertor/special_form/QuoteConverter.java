package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.QuoteExprNode;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class QuoteConverter {

    private QuoteConverter() {
    }

    public static SchemeExpression convert(SchemeList quoteList, @Nullable ParserRuleContext quoteCtx) {
        if (quoteList.size == 2) {
            var quoteExpr =  new QuoteExprNode(quoteList.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(quoteExpr, quoteCtx);
        } else {
            throw new SchemeException("quote: arity mismatch\nexpected: 1\ngiven: " + (quoteList.size - 1), null);
        }
    }
}
