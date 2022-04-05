package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MultiplyBinaryNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;


public class MultiplyExprNode extends SchemeExpression {

    @Child private SchemeExpression left;
    @Child private SchemeExpression right;
    @Child private BinaryOperationNode multiplyOperation = MultiplyBinaryNodeGen.create();

    public MultiplyExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return multiplyOperation.execute(left.executeGeneric(frame), right.executeGeneric(frame));
    }
}
