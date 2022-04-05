package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class DivideExprNode extends SchemeExpression {

    @Specialization
    protected Object divideDouble(double left, double right) {
        return left / right;
    }

    @Override
    public String toString() {
        return "/";
    }
}
