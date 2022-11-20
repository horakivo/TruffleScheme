package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class ModuloExprNode extends SchemeExpression {

    @Specialization
    protected long doLongs(long left, long right) {
        return left % right;
    }
}
