package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;


//TODO discover this idea
@NodeField(name = "frameIndex", type = int.class)
@NodeField(name = "frame", type = MaterializedFrame.class)
public abstract class ReadLocalVariableDirectlyExprNode extends SchemeExpression {

    protected abstract int getFrameIndex();

    protected abstract MaterializedFrame getFrame();

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong() {
        return getFrame().getLong(getFrameIndex());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean() {
        return getFrame().getBoolean(getFrameIndex());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected double readDouble() {
        return getFrame().getDouble(getFrameIndex());
    }

    //TODO         getFrame().getValue() discover differences
    @Specialization(rewriteOn = FrameSlotTypeException.class, replaces = {"readLong", "readBoolean", "readDouble"})
    protected Object read() {
        return getFrame().getObject(getFrameIndex());
    }

//    @Specialization(replaces = {"read"})
//    public Object read(VirtualFrame virtualFrame) {
//        return getFrame().getValue(getFrameIndex());
//    }
}

