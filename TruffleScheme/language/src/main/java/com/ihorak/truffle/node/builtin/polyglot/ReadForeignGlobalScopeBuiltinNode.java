package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.ReadForeignGlobalScopeCoreNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.ReadForeignGlobalScopeCoreNode.POLYGLOT_READ_GLOBAL_SCOPE;


public abstract class ReadForeignGlobalScopeBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected Object doReadForeignGlobalScope(Object[] arguments, @Cached ReadForeignGlobalScopeCoreNode readForeignGlobalScopeCoreNode) {
        return readForeignGlobalScopeCoreNode.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, POLYGLOT_READ_GLOBAL_SCOPE, 2, arguments.length);
    }
}
