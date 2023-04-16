package com.ihorak.truffle.node.exprs.bbuiltin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.ModuloCoreNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class ModuloBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected Object doModulo(Object[] arguments, @Cached ModuloCoreNode moduloCoreNode) {
        return moduloCoreNode.execute(arguments[0], arguments[1]);
    }

    @Specialization(guards = "arguments.length != 2")
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "modulo", 2, arguments.length);
    }
}
