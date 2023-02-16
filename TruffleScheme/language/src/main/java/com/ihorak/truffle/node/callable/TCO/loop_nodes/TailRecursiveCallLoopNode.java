package com.ihorak.truffle.node.callable.TCO.loop_nodes;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RepeatingNode;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class TailRecursiveCallLoopNode extends SchemeNode implements RepeatingNode {


    @Children
    private final SchemeExpression[] bodyExpressions;
    private final int argumentsIndex;
    private final ConditionProfile tailRecursion = ConditionProfile.create();

    private final CallTarget callTarget;
    private final FrameDescriptor frameDescriptor;

    public TailRecursiveCallLoopNode(SchemeExpression[] bodyExpressions, int argumentsIndex, FrameDescriptor frameDescriptor, CallTarget callTarget) {
        this.bodyExpressions = bodyExpressions;
        this.argumentsIndex = argumentsIndex;
        this.frameDescriptor = frameDescriptor;
        this.callTarget = callTarget;
    }

    @ExplodeLoop
    private Object executeImpl(VirtualFrame frame) {
        for (int i = 0; i < bodyExpressions.length - 1; i++) {
            bodyExpressions[i].executeGeneric(frame);
        }
        // return last element
        return bodyExpressions[bodyExpressions.length - 1].executeGeneric(frame);
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        throw CompilerDirectives.shouldNotReachHere();
    }

    @Override
    public Object executeRepeatingWithValue(final VirtualFrame frame) {
        try {
            Object[] arguments = (Object[]) frame.getObject(TCO_ARGUMENT_SLOT);
            var virtualFrame = Truffle.getRuntime().createVirtualFrame(arguments, frameDescriptor);
            return executeImpl(virtualFrame);
        } catch (SelfRecursiveTailCallException e) {
            return CONTINUE_LOOP_STATUS;
        }
    }

    @Override
    public boolean shouldContinue(final Object returnValue) {
        return returnValue == CONTINUE_LOOP_STATUS;
    }

}
