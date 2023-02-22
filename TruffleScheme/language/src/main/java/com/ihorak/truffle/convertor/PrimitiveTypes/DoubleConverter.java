package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;
import org.antlr.v4.runtime.Token;

public class DoubleConverter {

    private DoubleConverter() {
    }

    public static SchemeExpression convert(double value) {
        return new DoubleLiteralNode(value);
    }

    public static SchemeExpression convert(double value, Token doubleToken) {
        var expr = convert(value);
        SourceSectionUtil.setSourceSection(expr, doubleToken);

        return expr;
    }
}
