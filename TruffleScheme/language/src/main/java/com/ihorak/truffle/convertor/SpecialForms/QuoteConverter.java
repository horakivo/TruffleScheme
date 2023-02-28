package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.QuoteExprNode;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;

public class QuoteConverter {

    private QuoteConverter() {
    }

    public static SchemeExpression convert(SchemeList quoteList, ParserRuleContext quoteSrx) {
        if (quoteList.size == 2) {
            var quoteExpr =  new QuoteExprNode(quoteList.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(quoteExpr, quoteSrx);
        } else {
            throw new SchemeException("quote: arity mismatch\nexpected: 1\ngiven: " + (quoteList.size - 1), null);
        }
    }
}
