package com.ihorak.truffle.type;

import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;

@ExportLibrary(InteropLibrary.class)
public class PrimitiveProcedure implements TruffleObject {

    private final CallTarget callTarget;
    private final Integer numberOfArgs;
    //Because of the Interop library
    private final DispatchNode dispatchNode = DispatchNodeGen.create();

    public PrimitiveProcedure(CallTarget callTarget, Integer numberOfArgs) {
        this.callTarget = callTarget;
        this.numberOfArgs = numberOfArgs;
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    public Integer getNumberOfArgs() {
        return numberOfArgs;
    }

    //----------------InteropLibrary messages–----------------------
}
