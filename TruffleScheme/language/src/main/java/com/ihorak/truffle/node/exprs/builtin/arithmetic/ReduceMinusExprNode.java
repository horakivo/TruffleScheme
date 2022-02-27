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


/**
* In parser there should be convered cases when number of arguments are 0 or 1. This reduce node is responsible just
 * for number of arguments > 2. For number of arguments == 1 see {@link NegateNumberExprNode}
* */
public abstract class ReduceMinusExprNode extends BinaryReducibleOperation {


//    @Specialization(guards = "getArguments().length == 2")
//    protected long subtractTwoLongs(VirtualFrame frame) throws UnexpectedResultException {
//        var arguments = getArguments();
//        return getOperation().executeLong(arguments[0].executeLong(frame), arguments[1].executeLong(frame));
//    }
    //    @Specialization(guards = "getArguments().length == 1")
//    protected long oneLongArgument(VirtualFrame frame) throws UnexpectedResultException {
//        var argument = getArguments()[0].executeLong(frame);
//        return -argument;
//    }

//    @Specialization(guards = "getArguments().length == 0")
//    protected Object noArguments(VirtualFrame frame) {
//        throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
//    }


    @ExplodeLoop
    @Specialization(guards = "getArguments().length == cachedLength", limit = "2")
    protected Object subtractAnyNumberOfLongs(
            VirtualFrame frame,
            @Cached("getArguments().length") int cachedLength) {

        var operation = getOperation();
        var arguments = getArguments();
        Object result = arguments[0].executeGeneric(frame);

        for (int i = 1; i < cachedLength; i++) {
            result = operation.execute(result, arguments[i].executeGeneric(frame));
        }

        return result;
    }

    @Fallback
    protected Object fallback() {
        throw new SchemeException("-(reduce): this shouldn't happen I think");
    }
}
