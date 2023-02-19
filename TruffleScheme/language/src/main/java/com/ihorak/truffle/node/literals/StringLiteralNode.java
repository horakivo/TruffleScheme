package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;

public class StringLiteralNode extends SchemeExpression {

    private final TruffleString string;


    public StringLiteralNode(TruffleString string) {
        this.string = string;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return string;
    }
}
