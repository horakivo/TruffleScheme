package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.literals.StringLiteralNode;
import com.oracle.truffle.api.strings.TruffleString;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class TruffleStringConverter {

    private TruffleStringConverter() {
    }

    public static StringLiteralNode convert(TruffleString truffleString, ParserRuleContext ctx) {
        var expr = new StringLiteralNode(truffleString);
        SourceSectionUtil.setSourceSection(expr, ctx);

        return expr;
    }
}
