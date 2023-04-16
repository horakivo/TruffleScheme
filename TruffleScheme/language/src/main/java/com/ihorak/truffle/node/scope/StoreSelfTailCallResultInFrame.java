package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;


public abstract class StoreSelfTailCallResultInFrame extends SchemeExpression {

    @Child
    @Executed
    protected SchemeExpression valueToStore;
    @Child
    private WriteFrameSlotNode writeFrameSlotNode;
    private final int frameSlot;

    public StoreSelfTailCallResultInFrame(SchemeExpression valueToStore, int frameSlotIndex) {
        this.valueToStore = valueToStore;
        this.frameSlot = frameSlotIndex;
        this.writeFrameSlotNode = WriteFrameSlotNodeGen.create(frameSlot);
    }

    @Specialization
    protected Object storeResult(VirtualFrame frame, Object  valueToStore) {
        writeFrameSlotNode.executeWrite(frame, valueToStore);

        return UndefinedValue.SINGLETON;
    }
}
