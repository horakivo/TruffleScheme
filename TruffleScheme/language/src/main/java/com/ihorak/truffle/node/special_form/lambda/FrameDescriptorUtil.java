package com.ihorak.truffle.node.special_form.lambda;

import com.oracle.truffle.api.frame.VirtualFrame;

public class FrameDescriptorUtil {

    public static VirtualFrame findGlobalEnv(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;

        while (currentFrame.getArguments().length != 0) {
            currentFrame = (VirtualFrame) currentFrame.getArguments()[0];
        }

        return currentFrame;
    }
}
