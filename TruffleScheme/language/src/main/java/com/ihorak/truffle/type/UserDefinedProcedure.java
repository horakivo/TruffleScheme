package com.ihorak.truffle.type;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.utilities.CyclicAssumption;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public class UserDefinedProcedure implements TruffleObject {

    private int expectedNumberOfArgs;
    @CompilationFinal private RootCallTarget callTarget;
    private final MaterializedFrame parentFrame;

    /**
     * Manages the assumption that the {@link #callTarget} is stable.
     */
    private final CyclicAssumption callTargetStable;
    //Because of the Interop library
//    private final DispatchNode dispatchNode = DispatchNodeGen.create();


    public UserDefinedProcedure(RootCallTarget callTarget, int expectedNumberOfArgs, MaterializedFrame frame) {
        this.callTarget = callTarget;
        this.parentFrame = frame;
        this.expectedNumberOfArgs = expectedNumberOfArgs;
        this.callTargetStable = new CyclicAssumption("user procedure not redefined assumption");
//        if (hasOptionalArgs) {
//            this.expectedNumberOfArgs = expectedNumberOfArgs - 1;
//        } else {
//            this.expectedNumberOfArgs = expectedNumberOfArgs;
//        }
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

//    @ExportMessage
//    abstract static class Execute {
//        @Specialization
//        static Object dispatch(UserDefinedProcedure procedure, Object[] arguments, @Cached DispatchNode dispatchNode) {
//            return dispatchNode.executeDispatch(procedure, arguments);
//        }
//    }

    @ExportMessage
    Object execute(Object[] arguments, @Cached DispatchNode dispatchNode) {
        return dispatchNode.executeDispatch(this, arguments);

    }
//
//    @ExportMessage
//    Object execute(Object[] arguments) {
//        for (Object argument : arguments) {
//            if (!isSchemeValue(argument)) {
//                CompilerDirectives.transferToInterpreterAndInvalidate();
//                throw new SchemeException("'" + argument + "' is not an EasyScript value", null);
//            }
//        }
//        return this.dispatchNode.executeDispatch(getCallTarget(), arguments);
//    }
//
//
//    private boolean isSchemeValue(Object argument) {
//        return argument instanceof Long || argument instanceof Double || argument instanceof BigInteger || argument instanceof SchemeCell || argument instanceof UserDefinedProcedure || argument instanceof UndefinedValue;
//
//    }

}
