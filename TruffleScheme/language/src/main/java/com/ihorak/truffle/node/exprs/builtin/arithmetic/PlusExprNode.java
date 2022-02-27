package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class PlusExprNode extends BinaryOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long addLongs(long left, long right) {
        return Math.addExact(left, right);
    }

    @Specialization(replaces = "addLongs")
    protected BigInteger addBigInts(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Specialization
    protected double addDoubles(double left, double right) {
        return left + right;
    }

    @Override
    public String toString() {
        return "+";
    }
}
