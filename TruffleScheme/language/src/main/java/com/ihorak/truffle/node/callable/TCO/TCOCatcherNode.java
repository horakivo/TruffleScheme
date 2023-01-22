package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.LoopNode;

public class TCOCatcherNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] expressions;

    @Child
    private LoopNode loopNode;

    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;


    public TCOCatcherNode(final SchemeExpression[] expressions, int tailCallArgumentsSlot, int tailCallTargetSlot) {
        this.expressions = expressions;
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.loopNode = Truffle.getRuntime().createLoopNode(new TailCallLoopNode(tailCallArgumentsSlot, tailCallTargetSlot, null));
    }


    @ExplodeLoop
    private Object executeBody(VirtualFrame frame) {
        for (int i = 0; i < expressions.length - 1; i++) {
            expressions[i].executeGeneric(frame);
        }
        // return last element
        return expressions[expressions.length - 1].executeGeneric(frame);
    }

    @Override
    public Object executeGeneric(final VirtualFrame frame) {
        try {
            return executeBody(frame);
        } catch (TailCallException e) {
            return loopNode.execute(frame);
        }
    }
}
