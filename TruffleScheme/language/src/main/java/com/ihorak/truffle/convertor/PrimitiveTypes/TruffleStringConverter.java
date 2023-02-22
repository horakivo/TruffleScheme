package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.literals.StringLiteralNode;
import com.oracle.truffle.api.strings.TruffleString;
import org.antlr.v4.runtime.Token;

public class TruffleStringConverter {

    private TruffleStringConverter() {
    }

    public static StringLiteralNode convert(TruffleString truffleString) {
        return new StringLiteralNode(truffleString);
    }

    public static StringLiteralNode convert(TruffleString truffleString, Token stringToken) {
        var expr = convert(truffleString);
        SourceSectionUtil.setSourceSection(expr, stringToken);

        return expr;
    }
}
