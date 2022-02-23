package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class MinusExprRuntimeNode extends BinaryOperationNode {

    @Specialization
    protected long subtract(long left, long right) {
        return Math.subtractExact(left, right);
    }
}
