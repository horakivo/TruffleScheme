package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.builtin.BinaryReducibleOperation;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class ReduceMultiplyExprNode extends BinaryReducibleOperation {


    public ReduceMultiplyExprNode(SchemeExpression[] arguments, BinaryOperationNode operation) {
        super(arguments, operation);
    }

    @ExplodeLoop
    @Specialization(guards = "arguments.length == cachedLength", limit = "2")
    protected Object multiplyAnyNumberOfArgs(
            VirtualFrame frame,
            @Cached("arguments.length") int cachedLength) {
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
