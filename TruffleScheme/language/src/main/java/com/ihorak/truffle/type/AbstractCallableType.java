package com.ihorak.truffle.type;

import com.oracle.truffle.api.CallTarget;

public class AbstractCallableType {

    private final CallTarget callTarget;

    public AbstractCallableType(CallTarget callTarget) {
        this.callTarget = callTarget;
    }
}
