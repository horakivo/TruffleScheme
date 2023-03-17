package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "value")
public abstract class DisplayExprNode extends SchemeExpression {

    //TODO zde vratit undefined
    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected Object display(Object value) {
        System.out.println(value);
        return UndefinedValue.SINGLETON;
    }
}
