package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class BooleanConverter {

    private BooleanConverter() {
    }

    public static SchemeExpression convert(boolean bool, @Nullable ParserRuleContext ctx) {
        var expr = new BooleanLiteralNode(bool);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }
}
