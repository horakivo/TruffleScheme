package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

@NodeChild(value = "value")
public abstract class DisplayExprNode extends SchemeExpression {

    @Specialization
    @TruffleBoundary
    protected Object doDisplay(Object value) {
        getContext().getOutput().println(value);
        return UndefinedValue.SINGLETON;
    }
}
