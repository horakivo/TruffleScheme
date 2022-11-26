package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.node.literals.LongLiteralNode;

public class LongConverter {

    private LongConverter() {}

    public static LongLiteralNode convert(long value) {
        return new LongLiteralNode(value);
    }
}
