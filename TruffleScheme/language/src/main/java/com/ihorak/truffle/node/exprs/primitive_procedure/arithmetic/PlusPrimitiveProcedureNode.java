package com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltinExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class PlusPrimitiveProcedureNode extends ArbitraryBuiltinExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private BinaryOperationNode plusOperation = PlusBinaryNodeGen.create();


    @Specialization(guards = "arguments.length == 2")
    protected Object addTwoArguments(Object[] arguments) {
        return plusOperation.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = "cachedLength == arguments.length", limit = "2")
    protected Object addAnyNumberOfArgsFast(Object[] arguments,
                                               @Cached("arguments.length") int cachedLength) {
        Object result = 0L;

        for (int i = 0; i < cachedLength; i++) {
            result = plusOperation.execute(result, arguments[i]);
        }

        return result;
    }

    @Specialization(replaces = "addAnyNumberOfArgsFast")
    protected Object addAnyNumberOfArgs(Object[] arguments) {
        Object result = 0L;

        for (Object argument : arguments) {
            result = plusOperation.execute(result, argument);
        }

        return result;
    }

}
