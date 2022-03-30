package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.convertor.ListToExpressionConverter;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeMacro;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;

import java.util.List;

@NodeChild(value = "callable")
public abstract class CallableExprNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DispatchNode dispatchNode;
    private final ParsingContext parsingContext;


    private final BranchProfile macroWrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile userProcedureWrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile primitiveProcedureWrongNumberOfArgsProfile = BranchProfile.create();
    private final ConditionProfile conditionProfile = ConditionProfile.createBinaryProfile();


    public CallableExprNode(List<SchemeExpression> arguments, ParsingContext context) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.dispatchNode = DispatchNodeGen.create();
        this.parsingContext = context;
    }

    @Specialization
    protected Object doUserDefinedProcedure(VirtualFrame frame, SchemeFunction function) {
        var arguments = getProcedureArguments(function, frame);

        if (this.arguments.length < function.getExpectedNumberOfArgs()) {
            userProcedureWrongNumberOfArgsProfile.enter();
            throw new SchemeException("User defined procedure was called with wrong number of arguments." +
                    " \n Expected: " + function.getExpectedNumberOfArgs() +
                    " \n Given: " + this.arguments.length, this);
        }

        return call(function.getCallTarget(), arguments, frame);
    }

    @Specialization
    protected Object doPrimitiveProcedure(VirtualFrame frame, PrimitiveProcedure primitiveProcedure) {
        var arguments = getPrimitiveProcedureArgs(frame);

        var expectedNumberOfArgs = primitiveProcedure.getNumberOfArgs();
        if (expectedNumberOfArgs != null && expectedNumberOfArgs != this.arguments.length) {
            primitiveProcedureWrongNumberOfArgsProfile.enter();
            throw new SchemeException("Primitive procedure was called with wrong number of arguments." +
                    " \n Expected: " + expectedNumberOfArgs +
                    " \n Given: " + this.arguments.length, this);
        }

        return call(primitiveProcedure.getCallTarget(), arguments, frame);
    }

    @Specialization
    protected Object doMacro(VirtualFrame frame, SchemeMacro macro) {
        var transformationProcedure = macro.getTransformationProcedure();
        var macroArguments = getProcedureOrMacroArgsNoOptional(transformationProcedure, frame);

        if (transformationProcedure.getExpectedNumberOfArgs() != this.arguments.length) {
            macroWrongNumberOfArgsProfile.enter();
            throw new SchemeException("Procedure was called with wrong number of arguments." +
                    " \n Expected: " + transformationProcedure.getExpectedNumberOfArgs() +
                    " \n Given: " + this.arguments.length, this);
        }

        var transformedData = applyTransformationProcedure(macro.getTransformationProcedure(), macroArguments);

        return ListToExpressionConverter.convert(transformedData, parsingContext).executeGeneric(frame);
    }

    @Fallback
    protected Object fallback(Object object) {
        throw new SchemeException("application: not a procedure or macro;\nexpected: macro or procedure that can be applied to arguments\ngiven: " + object, this);
    }

    private Object[] getProcedureArguments(SchemeFunction function, VirtualFrame parentFrame) {
        if (conditionProfile.profile(function.isOptionalArgs())) {
            return getProcedureArgsWithOptional(function, parentFrame);
        } else {
            return getProcedureOrMacroArgsNoOptional(function, parentFrame);
        }
    }

    @ExplodeLoop
    private Object[] getProcedureArgsWithOptional(SchemeFunction function, VirtualFrame parentFrame) {
        // + 2 because first one is parent frame and second is the optional list
        Object[] args = new Object[function.getExpectedNumberOfArgs() + 2];
        args[0] = function.getParentFrame();

        int index = 1;
        for (int i = 0; i < function.getExpectedNumberOfArgs(); i++) {
            args[index] = arguments[i].executeGeneric(parentFrame);
            index++;
        }

        SchemeCell list = SchemeCell.EMPTY_LIST;
        for (int i = arguments.length - 1; i >= function.getExpectedNumberOfArgs(); i--) {
            list = list.cons(arguments[i].executeGeneric(parentFrame), list);
        }
        args[index] = list;

        return args;
    }

    @ExplodeLoop
    private Object[] getProcedureOrMacroArgsNoOptional(SchemeFunction function, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }

    @ExplodeLoop
    private Object[] getPrimitiveProcedureArgs(VirtualFrame frame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = frame.materialize();

        int index = 1;
        for (SchemeExpression expression : this.arguments) {
            args[index] = expression.executeGeneric(frame);
            index++;
        }

        return args;

    }

    private Object applyTransformationProcedure(SchemeFunction transformationProcedure, Object[] arguments) {
        return dispatchNode.executeDispatch(transformationProcedure.getCallTarget(), arguments);
    }

    private Object call(CallTarget callTarget, Object[] arguments, VirtualFrame frame) {
        //return dispatchNode.executeDispatch(callTarget, arguments);
        if (this.isTailRecursive()) {
            throw new TailCallException(callTarget, arguments);
        } else {
            while (true) {
                try {
                    return dispatchNode.executeDispatch(callTarget, arguments);
                } catch (TailCallException tailCallException) {
                    callTarget = tailCallException.getCallTarget();
                    arguments = tailCallException.getArguments();
                }
            }
        }
    }


}
