package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;

public class DoubleConverter {

    private DoubleConverter() {}

    public static SchemeExpression convert(double value) {
        return new DoubleLiteralNode(value);
    }
}
