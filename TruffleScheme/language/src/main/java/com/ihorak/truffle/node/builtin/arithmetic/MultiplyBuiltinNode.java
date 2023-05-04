package com.ihorak.truffle.node.builtin.arithmetic;

import com.ihorak.truffle.node.builtin.core.arithmetic.MultiplyBinaryNode;
import com.ihorak.truffle.node.builtin.core.arithmetic.MultiplyBinaryNodeGen;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.builtin.BinaryObjectOperationNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class MultiplyBuiltinNode extends AlwaysInlinableProcedureNode {


    @Specialization(guards = "arguments.length == 2")
    protected Object doTwoArgs(Object[] arguments,
                               @Cached MultiplyBinaryNode multiplyOperation) {
        return multiplyOperation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = "cachedLength == arguments.length", limit = "2")
    protected Object doArbitraryNumberOfArgsCached(Object[] arguments,
                                                   @Cached MultiplyBinaryNode multiplyOperation,
                                                   @Cached("arguments.length") int cachedLength) {
        Object result = 1L;

        for (int i = 0; i < cachedLength; i++) {
            result = multiplyOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(replaces = "doArbitraryNumberOfArgsCached")
    protected Object doUncached(Object[] arguments,
                                @Cached MultiplyBinaryNode multiplyOperation) {
        Object result = 0L;

        for (Object argument : arguments) {
            result = multiplyOperation.execute(result, argument);
        }

        return result;
    }
}
