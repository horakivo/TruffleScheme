package com.ihorak.truffle.node.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.list.CarCoreNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class CarBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected Object doCar(Object[] arguments, @Cached CarCoreNode carCoreNode) {
        return carCoreNode.execute(arguments[0]);
    }

    @Specialization(guards = "arguments.length != 1")
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "car", 1, arguments.length);
    }
}
