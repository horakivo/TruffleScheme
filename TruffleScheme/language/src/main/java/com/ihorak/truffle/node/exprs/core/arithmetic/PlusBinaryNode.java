package com.ihorak.truffle.node.exprs.core.arithmetic;

import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.math.BigInteger;

public abstract class PlusBinaryNode extends BinaryOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long addLongs(long left, long right) {
        return Math.addExact(left, right);
    }

    @TruffleBoundary
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
