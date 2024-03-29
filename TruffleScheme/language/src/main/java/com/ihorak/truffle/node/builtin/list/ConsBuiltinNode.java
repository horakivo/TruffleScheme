package com.ihorak.truffle.node.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.list.ConsCoreNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class ConsBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected Object doCons(Object[] arguments, @Cached ConsCoreNode consCoreNode) {
        return consCoreNode.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "cons", 2, arguments.length);
    }
}
