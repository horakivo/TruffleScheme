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


    public TailCallThrowerNode(final List<SchemeExpression> arguments, final SchemeExpression callable) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame, UserDefinedProcedure procedure) {
        var parentMaterializedFrame = (MaterializedFrame) frame.getArguments()[1];
        var args = getArguments(procedure, frame);
        var callTarget = procedure.getCallTarget();
        parentMaterializedFrame.setObject(TCO_ARGUMENT_SLOT, args);
        parentMaterializedFrame.setObject(TCO_CALLTARGET_SLOT, callTarget);
//        SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
//        target.target = procedure.getCallTarget();
//        target.arguments = getArguments(procedure, frame);
        throw TailCallException.INSTANCE;
//        throw new TailCallException(procedure.getCallTarget(), getArguments(procedure, frame));
    }

    @ExplodeLoop
    private Object[] getArguments(UserDefinedProcedure function, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 3];
        args[0] = function.getParentFrame();
        args[1] = parentFrame.getArguments()[1];
        args[2] = parentFrame.getArguments()[2];

        int index = 3;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }
}
