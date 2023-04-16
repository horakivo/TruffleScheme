package com.ihorak.truffle.node.builtin;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public abstract class DisplayBuiltinNode extends AlwaysInlinableProcedureNode {

    @TruffleBoundary
    @Specialization(guards = "arguments.length == 1")
    protected Object doDisplay(Object[] arguments) {
        getContext().getOutput().println(arguments[0]);
        return UndefinedValue.SINGLETON;
    }

    @Specialization
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "display", 1, arguments.length);
    }
}
