package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.ProcedureDispatchNode;
import com.ihorak.truffle.node.ProcedureDispatchNodeGen;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;

import java.util.List;

public class ProcedureCallExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child private SchemeExpression functionNode;
    @Children private final SchemeExpression[] arguments;
    @SuppressWarnings("FieldMayBeFinal")
    @Child private ProcedureDispatchNode dispatchNode;

    private final ConditionProfile noParentConditionProfile = ConditionProfile.createBinaryProfile();
    private final BranchProfile wrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile noFunctionAsFirstArgumentProfile = BranchProfile.create();


    public ProcedureCallExprNode(SchemeExpression functionNode, List<SchemeExpression> arguments) {
        this.functionNode = functionNode;
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        dispatchNode = ProcedureDispatchNodeGen.create();
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        SchemeFunction function = getProcedure(virtualFrame);
        Object[] arguments = getProcedureArguments(function, virtualFrame);

        if (function.getExpectedNumberOfArgs() != null && function.getExpectedNumberOfArgs() != this.arguments.length) {
            wrongNumberOfArgsProfile.enter();
            throw new SchemeException("Procedure was called with wrong number of arguments." +
                    " \n Expected: " + function.getExpectedNumberOfArgs() +
                    " \n Given: " + this.arguments.length);
        }

        return call(function.getCallTarget(), arguments, virtualFrame);
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

    private SchemeFunction getProcedure(VirtualFrame frame) {
        try {
            return functionNode.executeFunction(frame);
        } catch (UnexpectedResultException e) {
            noFunctionAsFirstArgumentProfile.enter();
            throw new SchemeException("application: not a procedure;\nexpected a procedure that can be applied to arguments\ngiven: " + e.getResult());
        }
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
}
