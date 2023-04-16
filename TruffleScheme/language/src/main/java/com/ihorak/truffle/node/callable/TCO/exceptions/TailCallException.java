package com.ihorak.truffle.node.callable.TCO.exceptions;

import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.nodes.ControlFlowException;

public class TailCallException extends ControlFlowException {

    public static final TailCallException INSTANCE = new TailCallException(null, null);

    private final UserDefinedProcedure userDefinedProcedure;
    private final Object[] arguments;

    public TailCallException(UserDefinedProcedure userDefinedProcedure, Object[] arguments) {
        this.userDefinedProcedure = userDefinedProcedure;
        this.arguments = arguments;
    }

    public UserDefinedProcedure getUserDefinedProcedure() {
        return userDefinedProcedure;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
