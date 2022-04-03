package com.ihorak.truffle.type;

import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;


/**
 * This Scheme type exist because I wanted to have seperated user define procedures from primitive ones
 * The reason behind it, is that user defined procedures will always have `numberOfArgs` specified (therefore it can be int instead of Integer)
 * Here it has to be Integer because there are primitive types (+, - ...) which take arbitrary number of args.
 *
 * Another reason is that this type doesn't have any VirtualFrame parent since this procedures do NOT create a VirtualFrame (in this implementation they do unfortunately, because I can't come up with better solution right now)
 *
 * */
@ExportLibrary(InteropLibrary.class)
public class PrimitiveProcedure implements TruffleObject {

    private final CallTarget callTarget;
    private final Integer numberOfArgs;
    private final String name;
    //Because of the Interop library
    private final DispatchNode dispatchNode = DispatchNodeGen.create();

    public PrimitiveProcedure(CallTarget callTarget, Integer numberOfArgs, String name) {
        this.callTarget = callTarget;
        this.numberOfArgs = numberOfArgs;
        this.name = name;
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    public Integer getNumberOfArgs() {
        return numberOfArgs;
    }

    public String getName() {
        return name;
    }

    //----------------InteropLibrary messages–----------------------
}
