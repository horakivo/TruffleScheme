package com.ihorak.truffle.node.builtin.core.arithmetic;

import com.ihorak.truffle.node.builtin.BinaryObjectOperationNode;
import com.ihorak.truffle.runtime.SchemeBigInt;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public abstract class MultiplyBinaryNode extends BinaryObjectOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    public long doLong(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    @TruffleBoundary
    @Specialization(replaces = "doLong")
    public SchemeBigInt doBigInt(SchemeBigInt left, SchemeBigInt right) {
        return new SchemeBigInt(left.getValue().multiply(right.getValue()));
    }

    @Specialization
    public double doDouble(double left, double right) {
        return left * right;
    }


    @Override
    public String toString() {
        return "*";
    }
}
