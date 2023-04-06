package com.ihorak.truffle.node.polyglot.TCO;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.LoopNode;

import java.util.List;

public abstract class PolyglotTailCallCatcherNode extends SchemeExpression {


    @Child private LoopNode loopNode;
    @Children private SchemeExpression[] arguments;
    @Executed
    @Child
    protected SchemeExpression callable;
    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;
    private final int tailCallResultSlot;

    public PolyglotTailCallCatcherNode(SchemeExpression callable, SchemeExpression[] arguments, int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.callable = callable;
        this.arguments = arguments;
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.tailCallResultSlot = tailCallResultSlot;
        this.loopNode = Truffle.getRuntime().createLoopNode(new PolyglotTailCallLoopNode(tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot));
    }

    @Specialization
    protected Object doPolyglotProcedure(VirtualFrame frame, Object foreignProcedure) {
        var args = getForeignArgs(frame);
        frame.setObject(tailCallTargetSlot, foreignProcedure);
        frame.setObject(tailCallArgumentsSlot, args);

        loopNode.execute(frame);

        return frame.getObject(tailCallResultSlot);
    }


    @ExplodeLoop
    private Object[] getForeignArgs(VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].executeGeneric(parentFrame);
        }

        return args;
    }
}
