package com.ihorak.truffle.node.builtin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.ApplyCoreNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class ApplyBuiltinNode extends AlwaysInlinableProcedureNode {

    //Object[] arguments are evaluated arguments for the apply -> (<procedure> (arg1) ... (argN) (list))

    @ExplodeLoop
    @Specialization(guards = {"argumentsLength == arguments.length", "arguments.length >= 2",}, limit = "2")
    protected Object doApplyCached(Object[] arguments,
                                   @Cached("arguments.length") int argumentsLength,
                                   @Cached ApplyCoreNode applyCoreNode) {
        Object[] optionalArguments = new Object[argumentsLength - 2];
        System.arraycopy(arguments, 1, optionalArguments, 0, argumentsLength - 2);

        return applyCoreNode.execute(arguments[0], optionalArguments, arguments[argumentsLength - 1]);
    }

    @Specialization(guards = "arguments.length >= 2", replaces = "doApplyCached", limit = "2")
    protected Object doApplyUncached(Object[] arguments,
                                     @Cached ApplyCoreNode applyCoreNode) {
        Object[] optionalArguments = new Object[arguments.length - 2];
        System.arraycopy(arguments, 1, optionalArguments, 0, arguments.length - 2);

        return applyCoreNode.execute(arguments[0], optionalArguments, arguments[arguments.length - 1]);
    }



    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityExceptionAtLeast(this, "apply", 2, arguments.length);
    }
}
