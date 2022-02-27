package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.builtin.BinaryReducibleOperation;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;


public abstract class ReducePlusExprNode extends BinaryReducibleOperation {
//    @ExplodeLoop
//    @Specialization(guards = "getArguments().length == cachedLength", limit = "2", rewriteOn = {UnexpectedResultException.class, ClassCastException.class})
//    protected long addAnyNumberOfLongs(
//            VirtualFrame frame,
//            @Cached("getArguments().length") int cachedLength) throws UnexpectedResultException {
//
//        long result = 0;
//        var operation = getOperation();
//        var arguments = getArguments();
//
//        for (int i = 0; i < cachedLength; i++) {
//            result = operation.executeLong(result, arguments[i].executeLong(frame));
//        }
//
//        return result;
//    }
//
//    @ExplodeLoop
//    @Specialization(replaces = "addAnyNumberOfLongs", guards = "getArguments().length == cachedLength ")
//    protected BigInteger addAnyNumberOfBigInts(
//            VirtualFrame frame,
//            @Cached("getArguments().length") int cachedLength) {
//
//        try {
//            BigInteger result = BigInteger.ZERO;
//            var operation = getOperation();
//            var arguments = getArguments();
//
//
//            for (int i = 0; i < cachedLength; i++) {
//                result = operation.executeBigInt(result, arguments[i].executeBigInt(frame));
//            }
//
//            return result;
//        } catch (UnexpectedResultException e) {
//            throw new SchemeException("+(reduction): Unsupported data type.\nExpected: long, BigInt\nGiven: " + e.getClass());
//        }
//    }

    @ExplodeLoop
    @Specialization(guards = "getArguments().length == cachedLength", limit = "2")
    protected Object addAnyNumberOfLongs(
            VirtualFrame frame,
            @Cached("getArguments().length") int cachedLength) {

        Object result = 0L;
        var operation = getOperation();
        var arguments = getArguments();

        for (int i = 0; i < cachedLength; i++) {
            result = operation.execute(result, arguments[i].executeGeneric(frame));
        }

        return result;
    }

    @Fallback
    protected Object fallback() {
        throw new SchemeException("+(reduce): this shouldn't happen I think");
    }
}
