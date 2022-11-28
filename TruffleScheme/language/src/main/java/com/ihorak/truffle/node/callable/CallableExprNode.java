package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;

import java.util.List;

public class CallableExprNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    @Child private SchemeExpression callable;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DispatchNode dispatchNode;
    // public final Object dataOperand;


    private final BranchProfile userProcedureWrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile primitiveProcedureWrongNumberOfArgsProfile = BranchProfile.create();
    private final ConditionProfile conditionProfile = ConditionProfile.createBinaryProfile();


    public CallableExprNode(List<SchemeExpression> arguments, SchemeExpression callable) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
        this.dispatchNode = DispatchNodeGen.create();
        // this.dataOperand = dataOperand;
    }

    @Override
    public Object executeGeneric(final VirtualFrame frame) {
        var function = (UserDefinedProcedure) callable.executeGeneric(frame);
        var args = getProcedureOrMacroArgsNoOptional(function, frame);

        return call(function.getCallTarget(), args);
    }

    @ExplodeLoop
    private Object[] getProcedureOrMacroArgsNoOptional(UserDefinedProcedure function, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }


    protected Object call(CallTarget callTarget, Object[] arguments) {
        return dispatchNode.executeDispatch(callTarget, arguments);
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
