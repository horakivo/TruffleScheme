package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.ConditionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class NotExprNode extends SchemeExpression {

    @Child
    private SchemeExpression expression;

    public NotExprNode(final SchemeExpression expression) {
        this.expression = expression;
    }

    @Override
    public boolean executeBoolean(final VirtualFrame virtualFrame) {
        var booleanValue = ConditionUtil.convertObjectToBoolean(expression.executeGeneric(virtualFrame));
        return !booleanValue;
    }

    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        return executeBoolean(virtualFrame);
    }
}
