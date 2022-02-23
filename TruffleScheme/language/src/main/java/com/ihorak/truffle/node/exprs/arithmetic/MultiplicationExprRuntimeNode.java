package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class MultiplicationExprRuntimeNode extends BinaryOperationNode {

    @Specialization
    public long doLong(long left, long right) {
        return Math.multiplyExact(left, right);
    }
}
