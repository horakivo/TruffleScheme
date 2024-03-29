package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.TailCallCatcherNode;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

import java.util.List;

public abstract class CallableExprNode extends SchemeExpression {

    @Children
    protected final SchemeExpression[] arguments;

    @Child
    @Executed
    protected SchemeExpression callable;

    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;
    private final int tailCallResultSlot;

    public CallableExprNode(List<SchemeExpression> arguments, SchemeExpression callable, int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.tailCallResultSlot = tailCallResultSlot;
        // this.dataOperand = dataOperand;
    }

    @Specialization
    protected Object doUserDefinedProcedure(VirtualFrame frame, UserDefinedProcedure procedure,
                                            @Cached DispatchUserProcedureNode dispatchUserProcedureNode) {
        var args = getProcedureArguments(procedure, arguments, frame);
        try {
            return dispatchUserProcedureNode.executeDispatch(procedure, args);
        } catch (TailCallException e) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            var tailCallCatcher = new TailCallCatcherNode(arguments, callable, tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot);
            return replace(tailCallCatcher).executeGeneric(frame);
        }
    }

    @Specialization
    protected Object doPrimitiveProcedure(
            VirtualFrame frame,
            PrimitiveProcedure procedure,
            @Cached DispatchPrimitiveProcedureNode dispatchNode) {
        var args = getArgumentsWithoutLexicalScope(arguments, frame);
        return dispatchNode.execute(procedure, args);
    }

    @Specialization(guards = "interopLib.isExecutable(interopProcedure)", limit = "getInteropCacheLimit()")
    protected Object doInteropProcedure(VirtualFrame frame,
                                        Object interopProcedure,
                                        @Cached DispatchUserProcedureNode dispatchUserProcedureNode,
                                        @CachedLibrary("interopProcedure") InteropLibrary interopLib) {
        return dispatchUserProcedureNode.executeDispatch(interopProcedure, getArgumentsWithoutLexicalScope(arguments, frame));
    }

    @Fallback
    protected Object fallback(Object procedure) {
        throw SchemeException.notProcedure(procedure, this);
    }


//    @Specialization
//    protected Object doUserDefinedProcedure(VirtualFrame frame, UserDefinedProcedure function) {
//        if (this.arguments.length < function.getExpectedNumberOfArgs()) {
//            userProcedureWrongNumberOfArgsProfile.enter();
//            throw new SchemeException("User defined procedure was called with wrong number of arguments." +
//                                              " \n Expected: " + function.getExpectedNumberOfArgs() +
//                                              " \n Given: " + this.arguments.length, this);
//        }
//
//        var args = getProcedureOrMacroArgsNoOptional(function, frame);
//
//        return call(function.getCallTarget(), args);
//    }

//    @Specialization
//    protected Object doPrimitiveProcedure(VirtualFrame frame, PrimitiveProcedure primitiveProcedure) {
//        var expectedNumberOfArgs = primitiveProcedure.getNumberOfArgs();
//        if (expectedNumberOfArgs != null && expectedNumberOfArgs != this.arguments.length) {
//            primitiveProcedureWrongNumberOfArgsProfile.enter();
//            throw new SchemeException(
//                    primitiveProcedure.getName() + ": arity mismatch; Expected number of arguments does not match the given number" +
//                            "\nexpected: " + expectedNumberOfArgs + "" +
//                            "\ngiven: " + this.arguments.length, this);
//        }
//
//        var args = getPrimitiveProcedureArgs(frame);
//        return call(primitiveProcedure.getCallTarget(), args);
//    }

//    @Fallback
//    protected Object fallback(Object object) {
//        throw new SchemeException(
//                "application: not a procedure or macro;\nexpected: macro or procedure that can be applied to arguments\ngiven: " + object, this);
//    }

//    private Object[] getUserDefinedProcedureArguments(UserDefinedProcedure function, VirtualFrame parentFrame) {
//        if (conditionProfile.profile(function.isOptionalArgs())) {
//            return getProcedureArgsWithOptional(function, parentFrame);
//        } else {
//            return getProcedureOrMacroArgsNoOptional(function, parentFrame);
//        }
//    }
//
//    @ExplodeLoop
//    private Object[] getProcedureArgsWithOptional(UserDefinedProcedure function, VirtualFrame parentFrame) {
//        // + 2 because first one is parent frame and second is the optional list
//        Object[] args = new Object[function.getExpectedNumberOfArgs() + 2];
//        args[0] = function.getParentFrame();
//
//        int index = 1;
//        for (int i = 0; i < function.getExpectedNumberOfArgs(); i++) {
//            args[index] = arguments[i].executeGeneric(parentFrame);
//            index++;
//        }
//
//        SchemeCell list = SchemeCell.EMPTY_LIST;
//        for (int i = arguments.length - 1; i >= function.getExpectedNumberOfArgs(); i--) {
//            list = list.cons(arguments[i].executeGeneric(parentFrame), list);
//        }
//        args[index] = list;
//
//        return args;
//    }


//    @ExplodeLoop
//    private Object[] getPrimitiveProcedureArgs(VirtualFrame frame) {
//        Object[] args = new Object[arguments.length + 1];
//        args[0] = frame.materialize();
//
//        int index = 1;
//        for (SchemeExpression expression : this.arguments) {
//            args[index] = expression.executeGeneric(frame);
//            index++;
//        }
//
//        return args;
//
//    }

//    @Override
//    public void setSelfTailRecursive(final List<SchemeSymbol> currentlyDefiningProcedures) {
//        this.isSelfTailRecursive = dataOperand instanceof SchemeSymbol symbol && currentlyDefiningProcedures.contains(symbol);
//    }

}
