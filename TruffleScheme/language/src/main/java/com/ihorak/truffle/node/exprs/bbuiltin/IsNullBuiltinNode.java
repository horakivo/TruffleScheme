package com.ihorak.truffle.node.exprs.bbuiltin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.IsNullCoreNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class IsNullBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected boolean doNull(Object[] arguments, @Cached IsNullCoreNode isNullCoreNode) {
        return isNullCoreNode.execute(arguments[0]);
    }


    @Specialization(guards = "arguments.length != 1")
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "null?", 1, arguments.length);
    }
}
