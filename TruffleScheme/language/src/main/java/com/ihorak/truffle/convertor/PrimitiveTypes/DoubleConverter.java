package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Nullable;

public class DoubleConverter {

    private DoubleConverter() {
    }

    public static SchemeExpression convert(double value, @Nullable ParserRuleContext ctx) {
        var expr = new DoubleLiteralNode(value);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }
}
