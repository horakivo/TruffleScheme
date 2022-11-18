package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.BranchProfile;
import org.jetbrains.annotations.NotNull;

public abstract class ContextualNode extends SchemeExpression {

    private final int lexicalScopeDepth;

    private final BranchProfile parentNotFound = BranchProfile.create();

    protected ContextualNode(final int lexicalScopeDepth) {
        this.lexicalScopeDepth = lexicalScopeDepth;
    }


    @ExplodeLoop
    protected VirtualFrame findCorrectVirtualFrame(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;
        for (int i = 0; i < lexicalScopeDepth; i++) {
            currentFrame = (VirtualFrame) getParentEnvironment(currentFrame);
        }

        return currentFrame;
    }

    @NotNull
    private Object getParentEnvironment(VirtualFrame virtualFrame) {
        if (virtualFrame.getArguments().length == 0) {
            parentNotFound.enter();
            throw new SchemeException("No parent found in ContextualNode! This is mistake in the parser!", this);
        }
        return virtualFrame.getArguments()[0];
    }
}
