package com.ihorak.truffle.node.builtin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.NotCoreNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class NotBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected boolean doNot(Object[] arguments, @Cached NotCoreNode notCoreNode) {
        return notCoreNode.execute(arguments[0]);
    }

    @Fallback
    protected boolean doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "not", 1, arguments.length);

    }
}
