package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BigIntLiteralNode;
import com.ihorak.truffle.type.SchemeBigInt;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;


public class BigIntConverter {


    private BigIntConverter() {
    }

    public static SchemeExpression convert(SchemeBigInt schemeBigInt, @Nullable ParserRuleContext ctx) {
        var expr = new BigIntLiteralNode(schemeBigInt);
        SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);

        return expr;
    }
}
