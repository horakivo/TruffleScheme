package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class BeginExprNode extends SchemeExpression {

    @Children private final SchemeExpression[] expressions;

    public BeginExprNode(List<SchemeExpression> expressions) {
        this.expressions = expressions.toArray(SchemeExpression[]::new);
    }



    @Override
    @ExplodeLoop
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        var size  = expressions.length;
        for (int i = 0; i < size - 1; i++) {
            expressions[i].executeGeneric(virtualFrame);
        }

        return expressions[size - 1].executeGeneric(virtualFrame);
    }
}
