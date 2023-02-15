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

//@ExportLibrary(InteropLibrary.class)
public class UserDefinedProcedure extends AbstractProcedure implements TruffleObject {

    private final int expectedNumberOfArgs;
    private final boolean optionalArgs;
    private final MaterializedFrame parentFrame;
    //Because of the Interop library
//    private final DispatchNode dispatchNode = DispatchNodeGen.create();

    public UserDefinedProcedure(CallTarget callTarget, int expectedNumberOfArgs, final boolean hasOptionalArgs, MaterializedFrame frame) {
        super(callTarget);
        this.parentFrame = frame;
        this.expectedNumberOfArgs = expectedNumberOfArgs;
//        if (hasOptionalArgs) {
//            this.expectedNumberOfArgs = expectedNumberOfArgs - 1;
//        } else {
//            this.expectedNumberOfArgs = expectedNumberOfArgs;
//        }
        this.optionalArgs = hasOptionalArgs;
    }


    public MaterializedFrame getParentFrame() {
        return parentFrame;
    }

    public int getExpectedNumberOfArgs() {
        return expectedNumberOfArgs;
    }

    public boolean isOptionalArgs() {
        return optionalArgs;
    }

    @Override
    public String toString() {
        return "#<user_procedure>";
    }

    //----------------InteropLibrary messages–----------------------
//
//    @ExportMessage
//    boolean hasLanguage() {
//        return true;
//    }
//
//    @ExportMessage
//    Class<? extends TruffleLanguage<?>> getLanguage() {
//        return SchemeTruffleLanguage.class;
//    }
//
//    @ExportMessage
//    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
//        return "#<procedure>";
//    }
//
//    @ExportMessage
//    boolean isExecutable() {
//        return true;
//    }
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
