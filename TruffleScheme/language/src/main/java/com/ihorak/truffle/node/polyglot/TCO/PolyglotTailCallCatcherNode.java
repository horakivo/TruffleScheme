package com.ihorak.truffle.node.polyglot.TCO;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.Node.Child;
import com.oracle.truffle.api.nodes.Node.Children;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailCallLoopNode;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.LoopNode;

public class PolyglotTailCallCatcherNode  extends SchemeExpression{


    @Child private LoopNode loopNode;
    @Children private SchemeExpression[] arguments;
    @Child private SchemeExpression callable;
    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;
    private final int tailCallResultSlot;

    public PolyglotTailCallCatcherNode(SchemeExpression callable, int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.callable = callable;
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.tailCallResultSlot = tailCallResultSlot;
        this.loopNode = Truffle.getRuntime().createLoopNode(new PolyglotTailCallLoopNode(tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot));
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return null;
    }
}
