package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.ProcedureDispatchNode;
import com.ihorak.truffle.node.ProcedureDispatchNodeGen;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import java.util.List;

public class ProcedureCallExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression functionNode;
    @SuppressWarnings("FieldMayBeFinal")
    @Children
    private SchemeExpression[] arguments;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private ProcedureDispatchNode dispatchNode;

    public ProcedureCallExprNode(SchemeExpression functionNode, List<SchemeExpression> arguments) {
        this.functionNode = functionNode;
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        //TODO Direct or indirect? I still dont understand properly which to use when
        dispatchNode = ProcedureDispatchNodeGen.create();
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        SchemeFunction function = getFunction(virtualFrame);
        Object[] arguments = getProcedureArguments(function, virtualFrame);

        if (function.getExpectedNumberOfArgs() != null && function.getExpectedNumberOfArgs() != this.arguments.length) {
            throw new SchemeException("Procedure was called with wrong number of arguments." +
                    " \n Expected: " + function.getExpectedNumberOfArgs() +
                    " \n Given: " + this.arguments.length);
        }

        return dispatchNode.executeDispatch(function, arguments);
    }

    private SchemeFunction getFunction(VirtualFrame frame) {
        try {
            return functionNode.executeFunction(frame);
        } catch (UnexpectedResultException e) {
            throw new IllegalArgumentException("FunctionNode is not a function in ProcedureCallExprNode" + e);
        }
    }

    private Object[] getProcedureArguments(SchemeFunction function, VirtualFrame parentFrame) {
        Object[] arguments = new Object[this.arguments.length + 1];
        if (function.getParentFrame() == null) {
            throw new SchemeException("User defined procedures should always have parent enviroment!");
        }
        arguments[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : this.arguments) {
            arguments[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return arguments;
    }

    /*For testing purposes*/

    public SchemeExpression[] getArguments() {
        return arguments;
    }
}
