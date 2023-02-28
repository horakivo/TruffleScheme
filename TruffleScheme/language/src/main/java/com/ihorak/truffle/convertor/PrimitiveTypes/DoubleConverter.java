package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class DoubleConverter {

    private DoubleConverter() {
    }

    public static SchemeExpression convert(double value, ParserRuleContext ctx) {
        var expr = new DoubleLiteralNode(value);
        SourceSectionUtil.setSourceSection(expr, ctx);

        return expr;
    }
}
