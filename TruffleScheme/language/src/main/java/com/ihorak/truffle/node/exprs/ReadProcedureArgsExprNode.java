package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class ReadProcedureArgsExprNode extends SchemeExpression {

    @ExplodeLoop
    @Specialization(guards = "cachedLength == frame.getArguments().length", limit = "5")
    protected Object getProceduresArgumentsFast(VirtualFrame frame,
                                                  @Cached("frame.getArguments().length") int cachedLength) {
        var frameArguments = frame.getArguments();
        Object[] arguments = new Object[cachedLength - 1];

        for (int i = 1; i < cachedLength; i++) {
            arguments[i - 1] = frameArguments[i];
        }

        return arguments;
    }

    @Specialization
    protected Object[] getProceduresArgumentsSlow(VirtualFrame frame) {
        var frameArguments = frame.getArguments();
        var frameArgumentSize = frameArguments.length;
        Object[] arguments = new Object[frameArgumentSize - 1];

        for (int i = 1; i < frameArgumentSize; i++) {
            arguments[i - 1] = frameArguments[i];
        }

        return arguments;
    }
}
