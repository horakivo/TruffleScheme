package com.ihorak.truffle.type;

import com.oracle.truffle.api.CallTarget;

public class AbstractProcedure {

    private final CallTarget callTarget;

    public AbstractProcedure(CallTarget callTarget) {
        this.callTarget = callTarget;
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }
}
