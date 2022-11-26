package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class TailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    @Child @Executed
    protected SchemeExpression callable;


    public TailCallThrowerNode(final List<SchemeExpression> arguments, final SchemeExpression callable) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame, UserDefinedProcedure procedure) {
        throw new TailCallException(procedure.getCallTarget(), gerArguments(procedure, frame));
    }


    @ExplodeLoop
    private Object[] gerArguments(UserDefinedProcedure function, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }
}
