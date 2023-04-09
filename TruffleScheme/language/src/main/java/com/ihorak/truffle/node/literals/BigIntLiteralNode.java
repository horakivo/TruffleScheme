package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public class BigIntLiteralNode extends SchemeExpression {

    private final SchemeBigInt value;

    public BigIntLiteralNode(SchemeBigInt value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return value.toString();
    }
}
