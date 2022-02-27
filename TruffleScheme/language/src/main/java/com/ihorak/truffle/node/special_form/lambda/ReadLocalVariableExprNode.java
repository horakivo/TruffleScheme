package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.SymbolExprNodeGen;
import com.ihorak.truffle.parser.ParserException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findAuxiliaryValue;

import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findGlobalEnv;


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

    @Specialization(guards = "isObject(frame)", replaces = {"readLong", "readBoolean", "readDouble"})
    protected Object read(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getObject(getFrameSlotIndex());
    }


    @Fallback
    protected void fallback(VirtualFrame frame) {
        throw new SchemeException(getSymbol() + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()));
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
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Long;
    }

    public boolean isBoolean(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Boolean;
    }

    public boolean isDouble(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Double;
    }

    public boolean isObject(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Object;
    }
}
