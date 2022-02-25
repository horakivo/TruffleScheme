package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class NewlineExprNode extends SchemeExpression {


    //TODO zde vratit undefined
    @Specialization
    protected Object newline() {
        System.out.println();
        return null;
    }
}
