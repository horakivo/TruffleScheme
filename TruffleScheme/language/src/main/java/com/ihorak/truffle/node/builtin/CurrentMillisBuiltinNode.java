package com.ihorak.truffle.node.builtin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class CurrentMillisBuiltinNode extends AlwaysInlinableProcedureNode {

    @TruffleBoundary
    @Specialization(guards = "arguments.length == 0")
    protected long getCurrentMillis(Object[] arguments) {
        return System.currentTimeMillis();
    }

    @Fallback
    protected long doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "current-milliseconds", 0, arguments.length);
    }
}
