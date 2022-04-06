package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import java.util.List;

public class ReduceComparisonExprNode extends SchemeExpression {

    @Children private final SchemeExpression[] comparisonExpression;

    public ReduceComparisonExprNode(List<SchemeExpression> comparisonExpression) {
        this.comparisonExpression = comparisonExpression.toArray(new SchemeExpression[0]);
    }

    @Override
    @ExplodeLoop
    public boolean executeBoolean(VirtualFrame virtualFrame) {
        boolean result = true;
        for (SchemeExpression expression : comparisonExpression) {
            try {
                if (!expression.executeBoolean(virtualFrame)) {
                    result = false;
                }
            } catch (UnexpectedResultException e) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new IllegalStateException("Parser mistake! This shouldn't happen!");
            }
        }

        return result;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeBoolean(virtualFrame);
    }
}
