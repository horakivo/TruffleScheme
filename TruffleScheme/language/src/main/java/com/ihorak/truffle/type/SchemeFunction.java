package com.ihorak.truffle.type;

import com.ihorak.truffle.node.*;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.interop.InteropLibrary;

import java.util.List;

@ExportLibrary(InteropLibrary.class)
public class SchemeFunction implements TruffleObject {

    private final CallTarget callTarget;
    private final Integer expectedNumberOfArgs;
    private MaterializedFrame parentFrame;
    private final ProcedureDispatchNode dispatchNode = ProcedureDispatchNodeGen.create();

    public SchemeFunction(CallTarget callTarget, Integer expectedNumberOfArgs) {
        this.callTarget = callTarget;
        this.expectedNumberOfArgs = expectedNumberOfArgs;
    }

    public void setParentFrame(MaterializedFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public MaterializedFrame getParentFrame() {
        return parentFrame;
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    public Integer getExpectedNumberOfArgs() {
        return expectedNumberOfArgs;
    }



}
