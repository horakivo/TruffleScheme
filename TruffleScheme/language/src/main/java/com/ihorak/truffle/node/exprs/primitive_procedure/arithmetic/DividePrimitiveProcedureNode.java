package com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.BinaryReducibleBuiltin;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.arithmetic.DivideBinaryNodeGen;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class DividePrimitiveProcedureNode extends BinaryReducibleBuiltin {

    @Child
    private BinaryOperationNode divideOperation = DivideBinaryNodeGen.create();

    @Specialization(guards = "arguments.length == 0")
    protected Object noArgs(Object[] arguments) {
        throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0", this);
    }

    @Specialization(guards = "arguments.length == 1")
    protected Object oneArg(Object[] arguments) {
        return divideOperation.execute(1.0, arguments[0]);
    }

    @ExplodeLoop
    @Specialization(guards = "cachedLength == arguments.length", limit = "2")
    protected Object divideAnyNumberOfArgsFast(Object[] arguments,
                                               @Cached("arguments.length") int cachedLength) {
        var result = arguments[0];

        for (int i = 1; i < cachedLength; i++) {
            result = divideOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(replaces = "divideAnyNumberOfArgsFast")
    protected Object divideAnyNumberOfArgs(Object[] arguments) {
        var result = arguments[0];

        for (int i = 1; i < arguments.length; i++) {
            result = divideOperation.execute(result, arguments[i]);
        }

        return result;
    }
}
