package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class ConvertSchemeExprsArgumentsNode extends SchemeExpression {

    @Children private final SchemeExpression[] arguments;

    public ConvertSchemeExprsArgumentsNode(List<SchemeExpression> arguments) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
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
