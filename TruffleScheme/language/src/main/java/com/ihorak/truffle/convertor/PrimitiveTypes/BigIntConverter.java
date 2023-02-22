package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BigIntLiteralNode;
import org.antlr.v4.runtime.Token;

import java.math.BigInteger;

public class BigIntConverter {


    private BigIntConverter() {
    }

    public static SchemeExpression convert(BigInteger bigInteger) {
        return new BigIntLiteralNode(bigInteger);
    }

    public static SchemeExpression convert(BigInteger bigInteger, Token bigIntToken) {
        var expr = convert(bigInteger);
        SourceSectionUtil.setSourceSection(expr, bigIntToken);

        return expr;
    }
}
