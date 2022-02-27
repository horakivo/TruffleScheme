package com.ihorak.truffle.exceptions;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.nodes.ControlFlowException;

public class TailCallException extends ControlFlowException {

    private final CallTarget callTarget;
    private final Object[] arguments;

    public TailCallException(CallTarget callTarget, Object[] arguments) {
        this.callTarget = callTarget;
        this.arguments = arguments;
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
