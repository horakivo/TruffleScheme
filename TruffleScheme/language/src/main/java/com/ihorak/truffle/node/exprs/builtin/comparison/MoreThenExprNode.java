//package com.ihorak.truffle.node.exprs.builtin.comparison;
//
//import com.ihorak.truffle.node.SchemeExpression;
//import com.ihorak.truffle.node.exprs.bbuiltin.BinaryBooleanOperationNode;
//import com.ihorak.truffle.node.exprs.core.comperison.MoreThenBinaryNodeGen;
//import com.oracle.truffle.api.frame.VirtualFrame;
//
//public class MoreThenExprNode extends SchemeExpression {
//
//    @Child private SchemeExpression left;
//    @Child private SchemeExpression right;
//    @Child private BinaryBooleanOperationNode moreThenOperation = MoreThenBinaryNodeGen.create();
//
//    public MoreThenExprNode(SchemeExpression left, SchemeExpression right) {
//        this.left = left;
//        this.right = right;
//    }
//
//    @Override
//    public boolean executeBoolean(VirtualFrame frame) {
//        return moreThenOperation.execute(left.executeGeneric(frame), right.executeGeneric(frame));
//    }
//
//    @Override
//    public Object executeGeneric(VirtualFrame frame) {
//        return executeBoolean(frame);
//    }
//}
