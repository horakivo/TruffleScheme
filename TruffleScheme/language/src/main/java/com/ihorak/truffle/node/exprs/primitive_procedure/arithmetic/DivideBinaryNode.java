package com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class DivideBinaryNode extends BinaryOperationNode {

    @Specialization
    protected Object divide(double left, double right) {
        return left / right;
    }

    @Override
    public String toString() {
        return "/";
    }
}
