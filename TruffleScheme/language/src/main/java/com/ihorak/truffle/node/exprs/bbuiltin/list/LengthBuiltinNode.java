package com.ihorak.truffle.node.exprs.bbuiltin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.list.LengthCoreNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LengthBuiltinNode extends AlwaysInlinableProcedureNode {


    @Specialization(guards = "arguments.length == 1")
    protected Object doLength(Object[] arguments, @Cached LengthCoreNode lengthCoreNode) {
        return lengthCoreNode.execute(arguments[0]);
    }

    @Specialization(guards = "arguments.length != 1")
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "length", 1, arguments.length);
    }
}
