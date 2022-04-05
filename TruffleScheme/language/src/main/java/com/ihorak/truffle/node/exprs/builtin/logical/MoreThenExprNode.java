package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.comperison.MoreThenBinaryNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;

public class MoreThenExprNode extends SchemeExpression {

    @Child private SchemeExpression left;
    @Child private SchemeExpression right;
    @Child private BinaryOperationNode moreThenOperation = MoreThenBinaryNodeGen.create();

    public MoreThenExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame) {
        return moreThenOperation.executeBoolean(left.executeGeneric(frame), right.executeGeneric(frame));
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return executeBoolean(frame);
    }
}
