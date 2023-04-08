package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.StringLiteralNode;
import com.oracle.truffle.api.strings.TruffleString;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class TruffleStringConverter {

    private TruffleStringConverter() {
    }

    public static SchemeExpression convert(TruffleString truffleString, @Nullable ParserRuleContext ctx) {
        var expr = new StringLiteralNode(truffleString);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }
}
