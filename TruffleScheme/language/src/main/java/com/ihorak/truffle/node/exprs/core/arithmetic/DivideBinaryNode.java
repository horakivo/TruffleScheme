package com.ihorak.truffle.node.exprs.core.arithmetic;

import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class DivideBinaryNode extends BinaryOperationNode {

    @Specialization
    protected double divide(double left, double right) {
        return left / right;
    }

    @Override
    public String toString() {
        return "/";
    }
}
