package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public class NewlineExprNode extends SchemeExpression {

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        System.out.println();
        return UndefinedValue.SINGLETON;
    }
}
