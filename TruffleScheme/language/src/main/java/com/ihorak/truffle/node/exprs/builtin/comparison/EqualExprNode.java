package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.comperison.EqualBinaryNode;
import com.ihorak.truffle.node.exprs.core.comperison.EqualBinaryNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;


public class EqualExprNode extends SchemeExpression {

    @Child private SchemeExpression left;
    @Child private SchemeExpression right;

    @Child private EqualBinaryNode equalOperation = EqualBinaryNodeGen.create();

    public EqualExprNode(final SchemeExpression left, final SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean executeBoolean(final VirtualFrame frame) {
        return equalOperation.execute(left.executeGeneric(frame), right.executeGeneric(frame));
    }

    @Override
    public Object executeGeneric(final VirtualFrame frame) {
        return executeBoolean(frame);
    }
}
