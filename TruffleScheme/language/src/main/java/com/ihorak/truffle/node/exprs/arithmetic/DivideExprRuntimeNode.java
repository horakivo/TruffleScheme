package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class DivideExprRuntimeNode extends BinaryOperationNode {

    @Specialization
    protected Object divide(double left, double right) {
        return left / right;
    }
}
