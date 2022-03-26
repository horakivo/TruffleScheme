package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.convertor.ListToExpressionConverter;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
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


    private final ConditionProfile noParentConditionProfile = ConditionProfile.createBinaryProfile();
    private final BranchProfile wrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile macroNoParentProfile = BranchProfile.create();


    public CallableExprNode(List<SchemeExpression> arguments, ParsingContext context) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.dispatchNode = DispatchNodeGen.create();
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

        if (function.getExpectedNumberOfArgs() != null && this.arguments.length < function.getExpectedNumberOfArgs()) {
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

    private Object[] getProcedureArguments(SchemeFunction function, VirtualFrame parentFrame) {
        if (function.isOptionalArgs()) {
            return getProcedureArgsWithOptional(function, parentFrame);
        } else {
            return getProcedureArgsNoOptional(function, parentFrame);
        }
    }

    @ExplodeLoop
    private Object[] getProcedureArgsWithOptional(SchemeFunction function, VirtualFrame parentFrame) {
        // + 2 because first one is parent frame and second is the optional list
        Object[] newArguments = new Object[function.getExpectedNumberOfArgs() + 2];
        //TODO maybe check here if it is not null
        newArguments[0] = function.getParentFrame();

        int index = 1;
        for (int i = 0; i < function.getExpectedNumberOfArgs(); i++) {
            newArguments[index] = arguments[i].executeGeneric(parentFrame);
            index++;
        }

        SchemeCell list = SchemeCell.EMPTY_LIST;
        for (int i = arguments.length - 1; i >= function.getExpectedNumberOfArgs(); i--) {
            list = list.cons(arguments[i].executeGeneric(parentFrame), list);
        }
        newArguments[index] = list;

        return newArguments;
    }

    @ExplodeLoop
    private Object[] getProcedureArgsNoOptional(SchemeFunction function, VirtualFrame parentFrame) {
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
