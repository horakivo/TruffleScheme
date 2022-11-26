package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BigIntLiteralNode;

import java.math.BigInteger;

public class BigIntConverter {


    private BigIntConverter() {}

    public static SchemeExpression convert(BigInteger bigInteger) {
        return new BigIntLiteralNode(bigInteger);
    }
}
