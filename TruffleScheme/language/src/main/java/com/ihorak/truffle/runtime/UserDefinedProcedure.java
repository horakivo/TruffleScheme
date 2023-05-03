package com.ihorak.truffle.runtime;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.callable.DispatchUserProcedureNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.source.SourceSection;


@ExportLibrary(InteropLibrary.class)
public record UserDefinedProcedure(
        int expectedNumberOfArgs,
        RootCallTarget callTarget,
        MaterializedFrame parentFrame,
        String name
) implements TruffleObject {


    /**
     * Manages the assumption that the {@link #callTarget} is stable.
     */
    //private final CyclicAssumption callTargetStable;


//    public void redefine(RootCallTarget callTarget, int expectedNumberOfArgs) {
//        if (this.callTarget != callTarget) {
//            CompilerDirectives.transferToInterpreterAndInvalidate();
//            this.callTarget = callTarget;
//            this.expectedNumberOfArgs = expectedNumberOfArgs;
//            callTargetStable.invalidate();
//        }
//    }

    // result of this method should be cached!
//    public Assumption getCallTargetStableAssumption() {
//        return callTargetStable.getAssumption();
//    }


    @TruffleBoundary
    @Override
    public String toString() {
        return "#<user_procedure>:" + name + ">" ;
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
    @TruffleBoundary
    SourceSection getSourceLocation() {
        return callTarget.getRootNode().getSourceSection();
    }

    @ExportMessage
    boolean hasSourceLocation() {
        return true;
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
    Object execute(Object[] arguments,
                   @Cached ForeignToSchemeNode foreignToSchemeNode,
                   @Cached DispatchUserProcedureNode dispatchUserProcedureNode) {
        var args = convertToSchemeValues(arguments, foreignToSchemeNode);
        return dispatchUserProcedureNode.executeDispatch(this, args);
    }

    private Object[] convertToSchemeValues(Object[] argumentsToConvert, ForeignToSchemeNode foreignToSchemeNode) {
        Object[] result = new Object[argumentsToConvert.length + 1];
        result[0] = null; // parent frame is not set in the foreign call
        int index = 1;
        for (Object argument : argumentsToConvert) {
            result[index] = foreignToSchemeNode.executeConvert(argument);
            index++;
        }

        return result;
    }

}
