package com.ihorak.truffle.type;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public class SchemeFunction implements TruffleObject {

    private final CallTarget callTarget;
    private final Integer expectedNumberOfArgs;
    private final boolean optionalArgs;
    private MaterializedFrame parentFrame;
    //Because of the Interop library
    private final DispatchNode dispatchNode = DispatchNodeGen.create();

    public SchemeFunction(CallTarget callTarget, Integer expectedNumberOfArgs, final boolean hasOptionalArgs) {
        this.callTarget = callTarget;
        if (hasOptionalArgs) {
            this.expectedNumberOfArgs = expectedNumberOfArgs - 1;
        } else {
            this.expectedNumberOfArgs = expectedNumberOfArgs;
        }
        this.optionalArgs = hasOptionalArgs;
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

    public boolean isOptionalArgs() {
        return optionalArgs;
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
