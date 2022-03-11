package com.ihorak.truffle.type;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.*;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;
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

    //----------------InteropLibrary messages–----------------------


    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return SchemeTruffleLanguage.class;
    }

    @ExportMessage
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return "#<procedure>";
    }

    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @ExportMessage
    Object execute(Object[] arguments) {
        for (Object argument : arguments) {
            if (!isSchemeValue(argument)) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new SchemeException("'" + argument + "' is not an EasyScript value");
            }
        }
        return this.dispatchNode.executeDispatch(this.callTarget, arguments);
    }


    private boolean isSchemeValue(Object argument) {
        return argument instanceof Long || argument instanceof Double || argument instanceof BigInteger || argument instanceof SchemeCell || argument instanceof SchemeFunction || argument instanceof UndefinedValue;

    }

}
