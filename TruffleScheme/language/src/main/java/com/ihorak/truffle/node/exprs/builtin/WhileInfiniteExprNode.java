package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class WhileInfiniteExprNode extends SchemeExpression {

    @Child
    private SchemeExpression expr;

    public WhileInfiniteExprNode(final SchemeExpression expr) {
        this.expr = expr;
    }

    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        while (true) {
            expr.executeGeneric(virtualFrame);
        }
    }
}
