package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class MultiplyExprNode extends BinaryOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    public long multipleLongs(long left, long right) {
        return Math.multiplyExact(left, right);
    }


    @Specialization(replaces = "multipleLongs")
    public BigInteger multiplyBigInts(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public String toString() {
        return "*";
    }
}
