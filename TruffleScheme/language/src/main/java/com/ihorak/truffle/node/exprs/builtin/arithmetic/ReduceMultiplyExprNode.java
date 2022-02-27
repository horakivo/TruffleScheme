package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.builtin.BinaryReducibleOperation;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class ReduceMultiplyExprNode extends BinaryReducibleOperation {


    @ExplodeLoop
    @Specialization(guards = "getArguments().length == cachedLength", limit = "2")
    protected Object multiplyAnyNumberOfArgs(
            VirtualFrame frame,
            @Cached("getArguments().length") int cachedLength) {
        var operation = getOperation();
        var arguments = getArguments();
        Object result = 1L;

        for (int i = 0; i < cachedLength; i++) {
            result = operation.execute(result, arguments[i].executeGeneric(frame));
        }

        return result;
    }

    @Fallback
    protected Object fallback() {
        throw new SchemeException("*(reduce): this shouldn't happen I think");
    }
}
