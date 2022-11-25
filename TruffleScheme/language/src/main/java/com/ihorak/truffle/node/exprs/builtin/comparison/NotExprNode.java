package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class NotExprNode extends SchemeExpression {


    @Child private BooleanCastExprNode value;

    public NotExprNode(final BooleanCastExprNode value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(final VirtualFrame frame) {
        return !value.executeBoolean(frame);
    }
}
