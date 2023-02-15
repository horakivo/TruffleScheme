package com.ihorak.truffle.node.callable.TCO.exceptions;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.nodes.ControlFlowException;

public class TailCallException extends ControlFlowException {

	public static final TailCallException INSTANCE = new TailCallException(null, null);
	
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
