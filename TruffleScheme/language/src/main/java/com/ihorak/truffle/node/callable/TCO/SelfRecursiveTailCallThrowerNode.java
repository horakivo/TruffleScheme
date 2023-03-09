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
    private final int tailRecursiveArgumentSlot;


    public SelfRecursiveTailCallThrowerNode(List<SchemeExpression> arguments, int tailRecursiveArgumentSlot) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.tailRecursiveArgumentSlot = tailRecursiveArgumentSlot;
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame) {
        prepareArgumentsForNextCall(frame);
        throw SelfRecursiveTailCallException.INSTANCE;
    }


    @ExplodeLoop
    private Object[] prepareArgumentsForNextCall(VirtualFrame frame) {
        Object[] args = (Object[]) frame.getObject(tailRecursiveArgumentSlot);

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(frame);
            index++;
        }

        return args;
    }
}
