package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigInteger;

public class BigIntLiteralNode extends SchemeExpression {

    private final BigInteger value;

    public BigIntLiteralNode(BigInteger value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public BigInteger executeBigInt(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString(10);
    }
}
