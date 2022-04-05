package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.comperison.EqualBinaryNodeGen;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import java.math.BigInteger;


public class EqualExprNode extends SchemeExpression {

    @Child private SchemeExpression left;
    @Child private SchemeExpression right;
    @Child private BinaryOperationNode equalOperation = EqualBinaryNodeGen.create();

    public EqualExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame)  {
        return equalOperation.executeBoolean(left.executeGeneric(frame), right.executeGeneric(frame));
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return executeBoolean(frame);
    }
}
