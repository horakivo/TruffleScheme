package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class TailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    @Child
    @Executed
    protected SchemeExpression callable;

    //This can be either name or e.g. list which will evaluate to procedure
    private final Object operand;


    public TailCallThrowerNode(final List<SchemeExpression> arguments, final SchemeExpression callable, Object operand) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
        this.operand = operand;
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame, UserDefinedProcedure procedure) {
        SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
        target.target = procedure.getCallTarget();
        target.arguments = getArguments(procedure, frame);
        throw TailCallException.INSTANCE;


        //throw new TailCallException(procedure.getCallTarget(), getArguments(procedure, frame));
    }

    @ExplodeLoop
    private Object[] getArguments(UserDefinedProcedure function, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }


    @Override
    public String toString() {
        return operand.toString();
    }
}
