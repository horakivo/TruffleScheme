package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.node.literals.StringLiteralNode;
import com.oracle.truffle.api.strings.TruffleString;

public class TruffleStringConverter {

    private TruffleStringConverter() {}

    public static StringLiteralNode convert(TruffleString truffleString) {
        return new StringLiteralNode(truffleString);
    }
}
