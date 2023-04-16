package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.frame.VirtualFrame;

public class UndefinedLiteralNode extends SchemeExpression {

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return UndefinedValue.SINGLETON;
    }
}
