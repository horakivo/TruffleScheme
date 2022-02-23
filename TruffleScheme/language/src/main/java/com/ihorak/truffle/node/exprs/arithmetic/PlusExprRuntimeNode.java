package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class PlusExprRuntimeNode extends BinaryOperationNode {

    @Specialization
    protected long addLongs(long left, long right) {
        return Math.addExact(left, right);
    }
}
