package com.ihorak.truffle.node.exprs.bbuiltin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.list.CdrCoreNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class CdrBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected Object doCdr(Object[] arguments, @Cached CdrCoreNode cdrCoreNode) {
        return cdrCoreNode.execute(arguments[0]);
    }

    @Specialization(guards = "arguments.length != 1")
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "cdr", 1, arguments.length);
    }
}
