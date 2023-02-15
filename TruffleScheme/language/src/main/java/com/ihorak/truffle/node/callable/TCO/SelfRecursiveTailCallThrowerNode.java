package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class SelfRecursiveTailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    @Child
    @Executed
    protected SchemeExpression callable;


    public SelfRecursiveTailCallThrowerNode(List<SchemeExpression> arguments, SchemeExpression callable) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame, UserDefinedProcedure procedure) {
        var arguments = getArguments(procedure, frame);
        var callTarget = procedure.getCallTarget();
        SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
        target.arguments = arguments;
        target.target = callTarget;
        throw SelfRecursiveTailCallException.INSTANCE;
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
}
