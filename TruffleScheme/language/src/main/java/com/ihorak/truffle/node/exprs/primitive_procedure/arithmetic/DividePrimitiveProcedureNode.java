package com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.exprs.ArbitraryNumberOfArgsBuiltin;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.arithmetic.DivideBinaryNodeGen;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class DividePrimitiveProcedureNode extends ArbitraryNumberOfArgsBuiltin {

    @Child
    private BinaryOperationNode divideOperation = DivideBinaryNodeGen.create();

    @Specialization(guards = "arguments.length == 0")
    protected Object noArgs(Object[] arguments) {
        throw SchemeException.arityException(this, "/", 1, 0);
    }

    @Specialization(guards = "arguments.length == 1")
    protected Object oneArg(Object[] arguments) {
        return divideOperation.execute(1.0, arguments[0]);
    }

    // TODO tady musi byt check ze to neni velikost 1
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
