package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import org.jetbrains.annotations.NotNull;

import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findGlobalEnv;


public abstract class ReadLocalVariableExprNode extends SchemeExpression {

    private final int lexicalScopeDepth;
    private final int frameSlotIndex;
    private final SchemeSymbol symbol;

    public ReadLocalVariableExprNode(int lexicalScopeDepth, int frameSlotIndex, SchemeSymbol symbol) {
        this.lexicalScopeDepth = lexicalScopeDepth;
        this.frameSlotIndex = frameSlotIndex;
        this.symbol = symbol;
    }

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.getLong(frameSlotIndex);
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.getBoolean(frameSlotIndex);
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) {
        var correctFrame = findCorrectVirtualFrame(frame);
        return correctFrame.getDouble(frameSlotIndex);
    }

    @Specialization(guards = "isObject(frame)", replaces = {"readLong", "readBoolean", "readDouble"})
    protected Object read(VirtualFrame frame)  {
        return findCorrectVirtualFrame(frame).getObject(frameSlotIndex);
    }


    @Fallback
    protected void fallback(VirtualFrame frame) {
        throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + findGlobalEnv(frame).getFrameDescriptor().getSlotKind(frameSlotIndex));
    }


    @NotNull
    private Object getParentEnvironment(VirtualFrame virtualFrame) {
        if (virtualFrame.getArguments().length > 0) {
            return virtualFrame.getArguments()[0];
        } else {
            CompilerDirectives.transferToInterpreter();
            throw new ParserException("This should never happen. It means that there is mistake in parser!");
        }
    }

    @ExplodeLoop
    private VirtualFrame findCorrectVirtualFrame(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;
        for (int i = 0; i < lexicalScopeDepth; i++) {
            currentFrame = (VirtualFrame) getParentEnvironment(currentFrame);
        }

        return currentFrame;
    }

    public boolean isLong(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Long;
    }

    public boolean isBoolean(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Boolean;
    }

    public boolean isDouble(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Double;
    }

    public boolean isObject(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Object;
    }
}
