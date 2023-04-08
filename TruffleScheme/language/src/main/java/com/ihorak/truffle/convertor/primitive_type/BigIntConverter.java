package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BigIntLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public class BigIntConverter {


    private BigIntConverter() {
    }

    public static SchemeExpression convert(BigInteger bigInteger, @Nullable ParserRuleContext ctx) {
        var expr = new BigIntLiteralNode(bigInteger);
        SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);

        return expr;
    }
}
