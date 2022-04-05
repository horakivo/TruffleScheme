package com.ihorak.truffle.node.exprs.core.arithmetic;

import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.math.BigInteger;

public abstract class MinusBinaryNode extends BinaryOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long doLongs(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @TruffleBoundary
    @Specialization(replaces = "doLongs")
    protected BigInteger doBigInts(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Specialization
    protected double doDoubles(double left, double right) {
        return left - right;
    }

    @Override
    public String toString() {
        return "-";
    }
}
