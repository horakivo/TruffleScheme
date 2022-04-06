package com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.BinaryReducibleBuiltin;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.arithmetic.MinusBinaryNodeGen;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.math.BigInteger;

public abstract class MinusPrimitiveProcedureNode extends BinaryReducibleBuiltin {

    @Child
    private BinaryOperationNode minusOperation = MinusBinaryNodeGen.create();

    @Specialization(guards = "arguments.length == 0")
    protected Object noArgs(Object[] arguments) {
        throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0", this);
    }

    @Specialization(guards = "isNegatableLong(arguments)")
    protected long oneArgLong(Object[] arguments) {
        return -((long) arguments[0]);
    }

    @TruffleBoundary
    @Specialization(guards = "isNegatableBigInt(arguments)")
    protected BigInteger oneArgBigInt(Object[] arguments) {
        return ((BigInteger) arguments[0]).negate();
    }

    @Specialization(guards = "isNegatableDouble(arguments)")
    protected double oneArgDouble(Object[] arguments) {
        return -((double) arguments[0]);
    }

    @ExplodeLoop
    @Specialization(guards = "arguments.length == cachedLength", limit = "2")
    protected Object subtractAnyNumberOfArgsFast(Object[] arguments,
                                                 @Cached("arguments.length") int cachedLength) {
        Object result = arguments[0];

        for (int i = 1; i < cachedLength; i++) {
            result = minusOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(replaces = "subtractAnyNumberOfArgsFast")
    protected Object subtractAnyNumberOfArgs(Object[] arguments) {
        Object result = arguments[0];

        for (int i = 1; i < arguments.length; i++) {
            result = minusOperation.execute(result, arguments[i]);
        }

        return result;
    }

    public boolean isNegatableLong(Object[] objs) {
        return objs.length == 1 && objs[0] instanceof Long;
    }

    public boolean isNegatableBigInt(Object[] objs) {
        return objs.length == 1 && objs[0] instanceof BigInteger;
    }

    public boolean isNegatableDouble(Object[] objs) {
        return objs.length == 1 && objs[0] instanceof Double;
    }


}
