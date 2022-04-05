package com.ihorak.truffle.node.exprs.primitive_procedure;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltinExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MultiplyBinaryNodeGen;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class MultiplyPrimitiveProcedureNode extends ArbitraryBuiltinExpression {

    @Child
    private BinaryOperationNode multiplyOperation = MultiplyBinaryNodeGen.create();

    @Specialization(guards = "arguments.length == 2")
    protected Object multiplyTwoArguments(Object[] arguments) {
        return multiplyOperation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = "cachedLength == arguments.length", limit = "2")
    protected Object multiplyAnyNumberOfArgsFast(Object[] arguments,
                                            @Cached("arguments.length") int cachedLength) {
        Object result = 1L;

        for (int i = 0; i < cachedLength; i++) {
            result = multiplyOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(replaces = "multiplyAnyNumberOfArgsFast")
    protected Object multiplyAnyNumberOfArgs(Object[] arguments) {
        Object result = 0L;

        for (Object argument : arguments) {
            result = multiplyOperation.execute(result, argument);
        }

        return result;
    }
}
