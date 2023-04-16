package com.ihorak.truffle.node.builtin.arithmetic;

import com.ihorak.truffle.node.builtin.core.arithmetic.PlusBinaryNode;
import com.ihorak.truffle.node.builtin.core.arithmetic.PlusBinaryNodeGen;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class PlusBuiltinNode extends AlwaysInlinableProcedureNode {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private PlusBinaryNode plusOperation = PlusBinaryNodeGen.create();


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
