package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import org.antlr.v4.runtime.Token;


public class LongConverter {

    private LongConverter() {
    }

    public static LongLiteralNode convert(long value) {
        return new LongLiteralNode(value);
    }

    public static LongLiteralNode convert(long value, Token longToken) {
        var expr = convert(value);
        SourceSectionUtil.setSourceSection(expr, longToken);

        return expr;
    }
}
