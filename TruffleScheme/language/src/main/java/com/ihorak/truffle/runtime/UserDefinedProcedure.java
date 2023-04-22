package com.ihorak.truffle.runtime;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
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
import com.oracle.truffle.api.utilities.CyclicAssumption;


@ExportLibrary(InteropLibrary.class)
public class UserDefinedProcedure implements TruffleObject {

    private int expectedNumberOfArgs;
    @CompilationFinal private RootCallTarget callTarget;
    private final MaterializedFrame parentFrame;
    private final String name;

    /**
     * Manages the assumption that the {@link #callTarget} is stable.
     */
    private final CyclicAssumption callTargetStable;


    public UserDefinedProcedure(RootCallTarget callTarget, int expectedNumberOfArgs, MaterializedFrame frame, String name) {
        this.callTarget = callTarget;
        this.parentFrame = frame;
        this.name = name;
        this.expectedNumberOfArgs = expectedNumberOfArgs;
        this.callTargetStable = new CyclicAssumption("user procedure not redefined assumption");

    }

    public void redefine(RootCallTarget callTarget, int expectedNumberOfArgs) {
        if (this.callTarget != callTarget) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.callTarget = callTarget;
            this.expectedNumberOfArgs = expectedNumberOfArgs;
            callTargetStable.invalidate();
        }
    }

    // result of this method should be cached!
    public Assumption getCallTargetStableAssumption() {
        return callTargetStable.getAssumption();
    }


    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public MaterializedFrame getParentFrame() {
        return parentFrame;
    }

    public int getExpectedNumberOfArgs() {
        return expectedNumberOfArgs;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "#<user_procedure>";
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
        return getCallTarget().getRootNode().getSourceSection();
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
                   @Cached DispatchNode dispatchNode) {
        var args = convertToSchemeValues(arguments, foreignToSchemeNode);
        return dispatchNode.executeDispatch(this, args);
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
