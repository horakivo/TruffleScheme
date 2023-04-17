package com.ihorak.truffle.node.builtin.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.arithmetic.DivideBinaryNodeGen;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.builtin.BinaryObjectOperationNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class DivideBuiltinNode extends AlwaysInlinableProcedureNode {

    @Child
    private BinaryObjectOperationNode divideOperation = DivideBinaryNodeGen.create();

    @Specialization(guards = "arguments.length == 2")
    protected Object doTwoArgs(Object[] arguments) {
        return divideOperation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = {"arguments.length >= 2", "cachedLength == arguments.length"}, limit = "2")
    protected Object doArbitraryNumberOfArgsCached(Object[] arguments,
                                               @Cached("arguments.length") int cachedLength) {
        var result = arguments[0];

        for (int i = 1; i < cachedLength; i++) {
            result = divideOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(guards = "arguments.length >= 2", replaces = "doArbitraryNumberOfArgsCached")
    protected Object doArbitraryNumberOfArgsUncached(Object[] arguments) {
        var result = arguments[0];

        for (int i = 1; i < arguments.length; i++) {
            result = divideOperation.execute(result, arguments[i]);
        }

        return result;
    }


    @Specialization(guards = "arguments.length == 1")
    protected Object doOneArg(Object[] arguments) {
        return divideOperation.execute(1L, arguments[0]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "/", 1, 0);
    }

}
