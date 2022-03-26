package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import java.util.List;

public class ReduceComparisonExprNode extends SchemeExpression {

    @Children private final SchemeExpression[] comparisonExpression;
    private final String name;

    public ReduceComparisonExprNode(List<SchemeExpression> comparisonExpression, String name) {
        this.comparisonExpression = comparisonExpression.toArray(new SchemeExpression[0]);
        this.name = name;
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
            } catch (UnsupportedSpecializationException exception) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new SchemeException(name + ": contract violation. Unsupported types! Left: " + exception.getSuppliedValues()[0] + " Right: " + exception.getSuppliedValues()[1], this);
            }
        }

        return result;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeBoolean(virtualFrame);
    }
}
