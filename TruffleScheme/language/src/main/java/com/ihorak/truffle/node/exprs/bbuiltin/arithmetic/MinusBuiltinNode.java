package com.ihorak.truffle.node.exprs.bbuiltin.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.BinaryObjectOperationNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.arithmetic.MinusBinaryNodeGen;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class MinusBuiltinNode extends AlwaysInlinableProcedureNode {

    @Child
    private BinaryObjectOperationNode minusOperation = MinusBinaryNodeGen.create();


    @Specialization(guards = "arguments.length == 2")
    protected Object doTwoArgs(Object[] arguments) {
        return minusOperation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = {"arguments.length >= 2", "arguments.length == cachedLength"}, limit = "2")
    protected Object doArbitraryNumberOfArgsCached(Object[] arguments,
                                                   @Cached("arguments.length") int cachedLength) {
        Object result = arguments[0];

        for (int i = 1; i < cachedLength; i++) {
            result = minusOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(guards = "arguments.length >= 2", replaces = "doArbitraryNumberOfArgsCached")
    protected Object doUncached(Object[] arguments) {
        Object result = arguments[0];

        for (int i = 1; i < arguments.length; i++) {
            result = minusOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(guards = {"arguments.length == 1", "isLong(arguments)"})
    protected long negateLong(Object[] arguments) {
        return -((long) arguments[0]);
    }

    @TruffleBoundary
    @Specialization(guards = {"arguments.length == 1", "isSchemeBigInt(arguments)"})
    protected SchemeBigInt oneArgBigInt(Object[] arguments) {
        return new SchemeBigInt(((SchemeBigInt) arguments[0]).getValue().negate());
    }

    @Specialization(guards = {"arguments.length == 1", "isDouble(arguments)"})
    protected double oneArgDouble(Object[] arguments) {
        return -((double) arguments[0]);
    }


    @Specialization(guards = "arguments.length == 0")
    protected Object noArgs(Object[] arguments) {
        throw SchemeException.arityException(this, "-", 1, 0);
    }

    protected static boolean isLong(Object[] arguments) {
        return arguments[0] instanceof Long;
    }

    protected static boolean isSchemeBigInt(Object[] arguments) {
        return arguments[0] instanceof SchemeBigInt;
    }

    protected static boolean isDouble(Object[] arguments) {
        return arguments[0] instanceof Double;
    }

}
