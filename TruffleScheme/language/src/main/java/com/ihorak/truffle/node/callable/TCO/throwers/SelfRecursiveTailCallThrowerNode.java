package com.ihorak.truffle.node.callable.TCO.throwers;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class SelfRecursiveTailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    @Children
    private final WriteFrameSlotNode[] writeFrameSlotNodes;


    public SelfRecursiveTailCallThrowerNode(List<SchemeExpression> arguments, List<WriteFrameSlotNode> writeFrameSlotNodes) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.writeFrameSlotNodes = writeFrameSlotNodes.toArray(WriteFrameSlotNode[]::new);
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame) {
        prepareArguments(frame);
        throw SelfRecursiveTailCallException.INSTANCE;
    }


    @ExplodeLoop
    private void prepareArguments(VirtualFrame frame) {
        Object[] evaluatedArgs = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            evaluatedArgs[i] = arguments[i].executeGeneric(frame);
        }

        for (int i = 0; i < writeFrameSlotNodes.length; i++) {
            writeFrameSlotNodes[i].executeWrite(frame, evaluatedArgs[i]);
        }
    }
}
