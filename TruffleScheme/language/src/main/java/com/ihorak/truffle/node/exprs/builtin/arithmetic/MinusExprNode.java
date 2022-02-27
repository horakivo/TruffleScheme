package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class MinusExprNode extends BinaryOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long subtractLongs(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @Specialization(replaces = "subtractLongs")
    protected BigInteger subtractBigInts(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public String toString() {
        return "-";
    }
}
