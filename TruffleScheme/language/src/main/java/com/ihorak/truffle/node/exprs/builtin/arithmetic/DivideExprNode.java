package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.BinaryExprNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.arithmetic.DivideBinaryNode;
import com.ihorak.truffle.node.exprs.core.arithmetic.DivideBinaryNodeGen;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;


public abstract class DivideExprNode extends BinaryExprNode {

    @Specialization
    protected Object doDouble(double left, double right, @Cached DivideBinaryNode divideOperation) {
        return divideOperation.execute(left, right);
    }
}
