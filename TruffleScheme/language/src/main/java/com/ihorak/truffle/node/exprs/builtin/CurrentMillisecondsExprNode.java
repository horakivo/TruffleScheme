package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class CurrentMillisecondsExprNode extends SchemeExpression {

    @Specialization
    protected long getCurrentMillis() {
        return System.currentTimeMillis();
    }
}
