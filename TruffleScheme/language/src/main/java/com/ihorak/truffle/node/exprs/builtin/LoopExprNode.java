package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LoopExprNode extends SchemeExpression {

    private final long n;
    @Child
    private SchemeExpression expr;

    public LoopExprNode(long n, SchemeExpression expr) {
        this.n = n;
        this.expr = expr;
    }


    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        Object result = null;
        for (int i = 0; i < n; i++) {
            result = expr.executeGeneric(virtualFrame);
        }

        return result;
    }
}
