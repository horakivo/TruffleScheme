package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import org.jetbrains.annotations.NotNull;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;


public abstract class ReadClosureVariableExprNode extends SchemeExpression {

    private final int lexicalScopeDepth;
    private final int frameSlotIndex;
    private final SchemeSymbol symbol;
    private final BranchProfile parentNotFound = BranchProfile.create();
    @CompilationFinal
    private FrameDescriptor frameDescriptor;

    public ReadClosureVariableExprNode(int lexicalScopeDepth, int frameSlotIndex, SchemeSymbol symbol) {
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
    protected Object read(VirtualFrame frame) {
        return findCorrectVirtualFrame(frame).getObject(frameSlotIndex);
    }


    @Fallback
    protected void fallback(VirtualFrame frame) {
        throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + findCorrectVirtualFrame(frame).getFrameDescriptor().getSlotKind(frameSlotIndex));
    }

    @NotNull
    private Object getParentEnvironment(VirtualFrame virtualFrame) {
        if (virtualFrame.getArguments().length == 0) {
            parentNotFound.enter();
            throw new IllegalStateException("No parent found in ReadLocalVariable! This is mistake in the parser!");
        }
        return virtualFrame.getArguments()[0];
    }

    @ExplodeLoop
    private VirtualFrame findCorrectVirtualFrame(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;
        for (int i = 0; i < lexicalScopeDepth; i++) {
            currentFrame = (VirtualFrame) getParentEnvironment(currentFrame);
        }

        return currentFrame;
    }

    @ExplodeLoop
    private FrameDescriptor findCorrectFrameDescriptor(VirtualFrame frame) {
        if (this.frameDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.frameDescriptor = findCorrectVirtualFrame(frame).getFrameDescriptor();
        }

        return frameDescriptor;
    }

    public boolean isLong(VirtualFrame frame) {
        return findCorrectFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Long;
    }

    public boolean isBoolean(VirtualFrame frame) {
        return findCorrectFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Boolean;
    }

    public boolean isDouble(VirtualFrame frame) {
        return findCorrectFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Double;
    }

    public boolean isObject(VirtualFrame frame) {
        return findCorrectFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Object;
    }
}
