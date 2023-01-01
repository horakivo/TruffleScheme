package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.comperison.EqualNumbersBinaryNode;
import com.ihorak.truffle.node.exprs.core.comperison.EqualNumbersBinaryNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;

public class EqualNumbersExprNode extends SchemeExpression {

    @Child private SchemeExpression left;
    @Child private SchemeExpression right;
    @Child private EqualNumbersBinaryNode equalOperation = EqualNumbersBinaryNodeGen.create();

    public EqualNumbersExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame)  {
        return equalOperation.execute(left.executeGeneric(frame), right.executeGeneric(frame));
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return equalOperation.execute(left.executeGeneric(frame), right.executeGeneric(frame));
    }
}