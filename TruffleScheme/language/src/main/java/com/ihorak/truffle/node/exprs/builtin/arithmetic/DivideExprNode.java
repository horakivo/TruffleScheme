package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.BinaryExprNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.arithmetic.DivideBinaryNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;


public abstract class DivideExprNode extends BinaryExprNode {

    @Specialization
    protected Object doDouble(double left, double right, @Cached DivideBinaryNode divideOperation) {
        return divideOperation.execute(left, right);
    }
}
