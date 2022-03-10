package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.ValueProfile;

public abstract class ReducePlusExprRuntimeNode extends SchemeExpression {

    private final BinaryOperationNode plusOperation;

    public ReducePlusExprRuntimeNode(BinaryOperationNode plusOperation) {
        this.plusOperation = plusOperation;
    }

    @ExplodeLoop
    @Specialization(guards = "cachedLength == frame.getArguments().length", limit = "2")
    protected Object addAnyNumberOfArgsRuntime(VirtualFrame frame,
                                               @Cached("frame.getArguments().length") int cachedLength) {
        var arguments = frame.getArguments();
        Object result = 0L;

        for (int i = 1; i < cachedLength; i++) {
            result = plusOperation.execute(result, arguments[i]);
        }

        return result;
    }
}
