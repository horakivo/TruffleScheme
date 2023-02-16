package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class SelfRecursiveTailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

//    @Child
//    @Executed
//    protected SchemeExpression callable;


    public SelfRecursiveTailCallThrowerNode(List<SchemeExpression> arguments) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
//        this.callable = callable;
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame) {
//        var parentMaterializedFrame = (MaterializedFrame) frame.getArguments()[2];
//        var args = getArguments(procedure, frame);
//        var callTarget = procedure.getCallTarget();
//        parentMaterializedFrame.setObject(TCO_ARGUMENT_SLOT, args);
//        parentMaterializedFrame.setObject(TCO_CALLTARGET_SLOT, callTarget);


        SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
        //target.target = procedure.getCallTarget();
        target.arguments = getArguments(frame);
        throw SelfRecursiveTailCallException.INSTANCE;
    }


    @ExplodeLoop
    private Object[] getArguments(VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = parentFrame.getArguments()[0];

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }
}
