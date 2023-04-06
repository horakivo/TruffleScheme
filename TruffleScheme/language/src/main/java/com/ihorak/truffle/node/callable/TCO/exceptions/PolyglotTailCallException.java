package com.ihorak.truffle.node.callable.TCO.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class PolyglotTailCallException extends ControlFlowException {

    private final Object polyglotProcedure;
    private final Object[] arguments;

    public PolyglotTailCallException(Object polyglotProcedure, Object[] arguments) {
        this.polyglotProcedure = polyglotProcedure;
        this.arguments = arguments;
    }

    public Object getPolyglotProcedure() {
        return polyglotProcedure;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
