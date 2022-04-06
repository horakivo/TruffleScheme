package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class ConvertSchemeExprsArgumentsNode extends SchemeExpression {

    @Children private final SchemeExpression[] arguments;

    public ConvertSchemeExprsArgumentsNode(SchemeExpression[] arguments) {
        this.arguments = arguments;
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] result = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            result[i] = arguments[i].executeGeneric(frame);
        }

        return result;
    }
}
