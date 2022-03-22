package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.ProcedureDispatchNode;
import com.ihorak.truffle.node.ProcedureDispatchNodeGen;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.ListToExpressionConverter;
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

    @Children private final SchemeExpression[] arguments;
    @SuppressWarnings("FieldMayBeFinal")
    @Child private ProcedureDispatchNode dispatchNode;
    private final Context parsingContext;


    private final ConditionProfile noParentConditionProfile = ConditionProfile.createBinaryProfile();
    private final BranchProfile wrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile macroNoParentProfile = BranchProfile.create();


    public CallableExprNode(List<SchemeExpression> arguments, Context context) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.dispatchNode = ProcedureDispatchNodeGen.create();
        this.parsingContext = context;
    }

    @Specialization
    protected Object doMacro(VirtualFrame frame, SchemeMacro macro) {
        var transformationProcedure = macro.getTransformationProcedure();
        var macroArguments = getMarcoArguments(transformationProcedure, frame);

        if (transformationProcedure.getExpectedNumberOfArgs() != null && transformationProcedure.getExpectedNumberOfArgs() != this.arguments.length) {
            wrongNumberOfArgsProfile.enter();
            throw new SchemeException("Procedure was called with wrong number of arguments." +
                    " \n Expected: " + transformationProcedure.getExpectedNumberOfArgs() +
                    " \n Given: " + this.arguments.length);
        }

        var transformedData = applyTransformationProcedure(macro.getTransformationProcedure(), macroArguments);

        return ListToExpressionConverter.convert(transformedData, parsingContext).executeGeneric(frame);
    }

    @Specialization
    protected Object doProcedure(VirtualFrame frame, SchemeFunction function) {
        var arguments = getProcedureArguments(function, frame);

        if (function.getExpectedNumberOfArgs() != null && function.getExpectedNumberOfArgs() != this.arguments.length) {
            wrongNumberOfArgsProfile.enter();
            throw new SchemeException("Procedure was called with wrong number of arguments." +
                    " \n Expected: " + function.getExpectedNumberOfArgs() +
                    " \n Given: " + this.arguments.length);
        }

        return call(function.getCallTarget(), arguments, frame);
    }

    @Fallback
    protected Object fallback(Object object) {
        throw new SchemeException("application: not a procedure or macro;\nexpected: macro or procedure that can be applied to arguments\ngiven: " + object);
    }

    @ExplodeLoop
    private Object[] getProcedureArguments(SchemeFunction function, VirtualFrame parentFrame) {
        Object[] arguments = new Object[this.arguments.length + 1];
        if (noParentConditionProfile.profile(function.getParentFrame() != null)) {
            arguments[0] = function.getParentFrame();
        } else {
            arguments[0] = parentFrame.materialize();
        }

        int index = 1;
        for (SchemeExpression expression : this.arguments) {
            arguments[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return arguments;
    }

    @ExplodeLoop
    private Object[] getMarcoArguments(SchemeFunction function, VirtualFrame parentFrame) {
        Object[] arguments = new Object[this.arguments.length + 1];
        if (function.getParentFrame() == null) {
            macroNoParentProfile.enter();
            throw new SchemeException("macro: no parent in lambda! Interpreter mistake!");
        }
        arguments[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : this.arguments) {
            arguments[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return arguments;
    }

    private Object applyTransformationProcedure(SchemeFunction transformationProcedure, Object[] arguments) {
        return dispatchNode.executeDispatch(transformationProcedure.getCallTarget(), arguments);
    }

    private Object call(CallTarget callTarget, Object[] arguments, VirtualFrame frame) {
        return dispatchNode.executeDispatch(callTarget, arguments);
//        if (this.isTailRecursive()) {
//            throw new TailCallException(callTarget, arguments);
//        } else {
//            while (true) {
//                try {
//                    return dispatchNode.executeDispatch(callTarget, arguments);
//                } catch (TailCallException tailCallException) {
//                    callTarget = tailCallException.getCallTarget();
//                    arguments = tailCallException.getArguments();
//                }
//            }
//        }
    }


}