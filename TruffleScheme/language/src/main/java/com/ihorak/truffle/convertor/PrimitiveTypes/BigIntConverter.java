package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BigIntLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.math.BigInteger;

public class BigIntConverter {


    private BigIntConverter() {
    }

    public static SchemeExpression convert(BigInteger bigInteger, ParserRuleContext ctx) {
        var expr = new BigIntLiteralNode(bigInteger);
        SourceSectionUtil.setSourceSection(expr, ctx);

        return expr;
    }
}
