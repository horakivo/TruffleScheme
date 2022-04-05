package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.arithmetic.DivideBinaryNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;


public class DivideExprNode extends SchemeExpression {

    @Child private SchemeExpression left;
    @Child private SchemeExpression right;
    @Child private BinaryOperationNode divideOperation = DivideBinaryNodeGen.create();

    public DivideExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return divideOperation.execute(left.executeGeneric(frame), right.executeGeneric(frame));
    }
}
