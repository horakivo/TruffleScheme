package com.ihorak.truffle.node.exprs.primitive_procedure.comparison;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.exprs.core.BinaryBooleanOperationNode;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;


public abstract class ReduceComparisonPrimitiveProcedureNode extends SchemeNode {

    public abstract boolean execute(Object[] arguments, BinaryBooleanOperationNode operation);

    @Specialization(guards = "arguments.length == 1")
    protected boolean oneArg(Object[] arguments, BinaryBooleanOperationNode operation) {
        return true;
    }

    @Specialization(guards = "arguments.length == 2")
    protected boolean twoArgs(Object[] arguments, BinaryBooleanOperationNode operation) {
        return operation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = "cachedLength == arguments.length", limit = "2")
    protected boolean arbitraryNumberOfArgsFast(Object[] arguments, BinaryBooleanOperationNode operation,
                                                @Cached("arguments.length") int cachedLength) {
        var result = true;
        for (int i = 0; i < cachedLength - 1; i++) {
            if (!operation.execute(arguments[i], arguments[i + 1])) {
                result = false;
            }
        }

        return result;
    }

    @Specialization
    protected boolean arbitraryNumberOfArgs(Object[] arguments, BinaryBooleanOperationNode operation) {
        var result = true;
        for (int i = 0; i < arguments.length - 1; i++) {
            if (!operation.execute(arguments[i], arguments[i + 1])) {
                result = false;
            }
        }

        return result;
    }

}
