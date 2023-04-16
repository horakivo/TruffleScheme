package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.frame.VirtualFrame;


public class WriteLocalVariableExprNode extends SchemeExpression {

    @Child private WriteFrameSlotNode writeFrameSlotNode;
    @Child protected SchemeExpression valueNode;

    public WriteLocalVariableExprNode(int frameIndex, SchemeExpression valueToStore) {
        this.writeFrameSlotNode = WriteFrameSlotNodeGen.create(frameIndex);
        this.valueNode = valueToStore;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        final Object value = valueNode.executeGeneric(frame);
        writeFrameSlotNode.executeWrite(frame, value);
        return UndefinedValue.SINGLETON;
    }
}
