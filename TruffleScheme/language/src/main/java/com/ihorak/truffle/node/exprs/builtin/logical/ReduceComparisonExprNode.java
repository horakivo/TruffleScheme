package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.node.exprs.builtin.BinaryReducibleOperation;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;


public abstract class ReduceComparisonExprNode extends BinaryReducibleOperation {

    @ExplodeLoop
    @Specialization(guards = "getArguments().length == cachedLength", limit = "2")
    protected boolean reduce(
            VirtualFrame frame,
            @Cached("getArguments().length") int cachedLength) {

        var arguments = getArguments();
        var operation = getOperation();

        for (int i = 0; i < cachedLength - 1; i++) {
            if (!(boolean) operation.execute(arguments[i].executeGeneric(frame), arguments[i + 1].executeGeneric(frame))) {
                return false;
            }
        }

        return true;
    }
}
