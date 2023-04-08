package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;


public class LongConverter {

    private LongConverter() {
    }

    public static SchemeExpression convert(long value, @Nullable ParserRuleContext ctx) {
        var node =  new LongLiteralNode(value);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(node, ctx);
    }

}
