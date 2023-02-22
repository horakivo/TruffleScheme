package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import org.antlr.v4.runtime.Token;

public class BooleanConverter {

    private BooleanConverter() {
    }

    public static SchemeExpression convert(boolean bool) {
        return new BooleanLiteralNode(bool);
    }

    public static SchemeExpression convert(boolean bool, Token booleanToken) {
        var expr = convert(bool);
        SourceSectionUtil.setSourceSection(expr, booleanToken);

        return expr;
    }

}
