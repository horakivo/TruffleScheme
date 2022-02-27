package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "value")
public abstract class DivideOneArgumentExprNode extends SchemeExpression {

    @Specialization
    protected double doLong(long value) {
        return 1 / Long.valueOf(value).doubleValue();
    }

    @Specialization
    protected double doDouble(double value) {
        return 1 / value;
    }
}
