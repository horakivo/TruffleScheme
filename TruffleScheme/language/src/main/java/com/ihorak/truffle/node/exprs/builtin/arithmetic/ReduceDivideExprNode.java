package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.builtin.BinaryReducibleOperation;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;


public abstract class ReduceDivideExprNode extends BinaryReducibleOperation {

//
//    @Specialization(guards = "getArguments().length == 2")
//    protected double divideTwoDoubles(VirtualFrame frame) throws UnexpectedResultException {
//        var arguments = getArguments();
//        return getOperation().executeDouble(arguments[0].executeDouble(frame), arguments[1].executeDouble(frame));
//    }

//    @Specialization(guards = "getArguments().length == 0")
//    protected Object noArguments(VirtualFrame frame) {
//        throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
//    }
//
//    @Specialization(guards = "getArguments().length == 1")
//    protected double oneArgument(VirtualFrame frame) throws UnexpectedResultException {
//        var argument = getArguments()[0].executeDouble(frame);
//        return 1 / argument;
//
//    }

    @ExplodeLoop
    @Specialization(guards = "getArguments().length == cachedLength", limit = "2")
    protected Object divideAnyNumberOfArguments(
            VirtualFrame frame,
            @Cached("getArguments().length") int cachedLength) {
        var operation = getOperation();
        var arguments = getArguments();
        var result = arguments[0].executeGeneric(frame);

        for (int i = 1; i < cachedLength; i++) {
            result = operation.execute(result, arguments[i].executeGeneric(frame));
        }

        return result;
    }

    @Fallback
    protected Object fallback() {
        throw new SchemeException("/(reduce): this shouldn't happen I think");
    }
}
