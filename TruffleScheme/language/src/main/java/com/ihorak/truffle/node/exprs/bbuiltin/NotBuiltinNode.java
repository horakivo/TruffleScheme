package com.ihorak.truffle.node.exprs.bbuiltin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.NotCoreNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class NotBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected boolean doNot(Object[] arguments, @Cached NotCoreNode notCoreNode) {
        return notCoreNode.execute(arguments[0]);
    }

    @Specialization(guards = "arguments.length != 1")
    protected boolean doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "not", 1, arguments.length);

    }
}
