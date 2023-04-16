package com.ihorak.truffle.node.exprs.builtin;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class CurrentMillisBuiltinNode extends AlwaysInlinableProcedureNode {

    @TruffleBoundary
    @Specialization(guards = "arguments.length == 0")
    protected long getCurrentMillis(Object[] arguments) {
        return System.currentTimeMillis();
    }
}
