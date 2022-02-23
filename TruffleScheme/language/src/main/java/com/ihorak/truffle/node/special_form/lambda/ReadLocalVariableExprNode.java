package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.ParserException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;


@NodeField(name = "symbol", type = SchemeSymbol.class)
@NodeField(name = "frameSlotIndex", type = int.class)
@NodeField(name = "lexicalScopeDepth", type = int.class)
public abstract class ReadLocalVariableExprNode extends SchemeExpression {

    protected abstract int getFrameSlotIndex();

    protected abstract int getLexicalScopeDepth();

    protected abstract SchemeSymbol getSymbol();

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.getLong(getFrameSlotIndex());
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.getBoolean(getFrameSlotIndex());
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.getDouble(getFrameSlotIndex());
    }

    @Specialization(replaces = {"readLong", "readBoolean", "readDouble"})
    protected Object read(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        if (!correctFrame.isObject(getFrameSlotIndex())) {
            throw new SchemeException("TODO zjistit co udelat, viz. simple language");
        } else {
            return correctFrame.getObject(getFrameSlotIndex());
        }
    }


    private Object getParentEnvironment(VirtualFrame virtualFrame) {
        if (virtualFrame.getArguments().length > 0) {
            return virtualFrame.getArguments()[0];
        }
        return null;
    }

    private VirtualFrame findCorrectVirtualFrame(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;
        for (int i = 0; i < getLexicalScopeDepth(); i++) {
            var parentFrame = (VirtualFrame) getParentEnvironment(currentFrame);
            if (parentFrame == null) {
                throw new ParserException("This should never happen. It means that there is mistake in parser!");
            }
            currentFrame = parentFrame;
        }

        return currentFrame;
    }

    public boolean isLong(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.isLong(getFrameSlotIndex());
    }

    public boolean isBoolean(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.isBoolean(getFrameSlotIndex());
    }

    public boolean isDouble(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.isDouble(getFrameSlotIndex());
    }
}
