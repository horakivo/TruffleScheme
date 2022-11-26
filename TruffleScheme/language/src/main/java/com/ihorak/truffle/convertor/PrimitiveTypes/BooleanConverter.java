package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;

public class BooleanConverter {

    private BooleanConverter() {}

    public static SchemeExpression convert(boolean bool) {
        return new BooleanLiteralNode(bool);
    }

}
