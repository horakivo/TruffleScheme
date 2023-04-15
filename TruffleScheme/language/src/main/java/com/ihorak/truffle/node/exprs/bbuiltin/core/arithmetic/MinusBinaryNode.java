package com.ihorak.truffle.node.exprs.bbuiltin.core.arithmetic;

import com.ihorak.truffle.node.exprs.bbuiltin.BinaryObjectOperationNode;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;


public abstract class MinusBinaryNode extends BinaryObjectOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long doLongs(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @TruffleBoundary
    @Specialization(replaces = "doLongs")
    protected SchemeBigInt doBigInts(SchemeBigInt left, SchemeBigInt right) {
        return new SchemeBigInt(left.getValue().subtract(right.getValue()));
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
