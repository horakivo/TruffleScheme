package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameUtil {

    public static VirtualFrame findGlobalEnv(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;

        while (currentFrame.getArguments().length != 0) {
            currentFrame = (VirtualFrame) currentFrame.getArguments()[0];
        }

        return currentFrame;
    }

    @NotNull
    public static Object findAuxiliaryValue(VirtualFrame frame, SchemeSymbol symbol) {
        var index = frame.getFrameDescriptor().getAuxiliarySlots().get(symbol);

        if (index != null) {
            return frame.getAuxiliarySlot(index);
        }

        var parentFrame = getParentEnvironment(frame);
        if (parentFrame == null) {
            throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition");
        }
        return findAuxiliaryValue((VirtualFrame) parentFrame, symbol);
    }

    @Nullable
    private static Object getParentEnvironment(VirtualFrame virtualFrame) {
        if (virtualFrame.getArguments().length > 0) {
            return virtualFrame.getArguments()[0];
        }
        return null;
    }
}
