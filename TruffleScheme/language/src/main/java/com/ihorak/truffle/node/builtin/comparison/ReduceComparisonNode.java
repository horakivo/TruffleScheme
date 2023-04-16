package com.ihorak.truffle.node.builtin.comparison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.BinaryBooleanOperationNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;


public abstract class ReduceComparisonNode extends SchemeNode {

    public abstract boolean execute(Object[] arguments, BinaryBooleanOperationNode operation, String name);

    @Specialization(guards = "arguments.length == 2")
    protected boolean doTwoArgs(Object[] arguments, BinaryBooleanOperationNode operation, String name) {
        return operation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = {"arguments.length >= 2", "cachedLength == arguments.length"}, limit = "2")
    protected boolean doCached(Object[] arguments, BinaryBooleanOperationNode operation, String name,
                               @Cached("arguments.length") int cachedLength) {
        var result = true;
        for (int i = 0; i < cachedLength - 1; i++) {
            if (!operation.execute(arguments[i], arguments[i + 1])) {
                result = false;
            }
        }

        return result;
    }

    @Specialization(guards = "arguments.length >= 2", replaces = "doCached")
    protected boolean doUncached(Object[] arguments, BinaryBooleanOperationNode operation, String name) {
        var result = true;
        for (int i = 0; i < arguments.length - 1; i++) {
            if (!operation.execute(arguments[i], arguments[i + 1])) {
                result = false;
            }
        }

        return result;
    }

    @Specialization(guards = "arguments.length == 1")
    protected boolean doOneArg(Object[] arguments, BinaryBooleanOperationNode operation, String name) {
        return true;
    }

    @Specialization(guards = "arguments.length == 0")
    protected boolean doThrow(Object[] arguments, BinaryBooleanOperationNode operation, String name) {
        throw SchemeException.arityException(this, name, 1, 0);
    }
}
