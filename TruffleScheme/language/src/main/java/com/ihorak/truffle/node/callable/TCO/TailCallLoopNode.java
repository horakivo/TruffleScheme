package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailCallLoopNode extends SchemeNode implements RepeatingNode {

    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();
    private CallTarget callTarget;
    private Object[] arguments;

    public TailCallLoopNode(final CallTarget callTarget, final Object[] arguments) {
        this.callTarget = callTarget;
        this.arguments = arguments;
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        throw CompilerDirectives.shouldNotReachHere();
    }

    @Override
    public Object executeRepeatingWithValue(final VirtualFrame frame) {
        try {
            return dispatchNode.executeDispatch(callTarget, arguments);
        } catch (TailCallException e) {
            this.callTarget = e.getCallTarget();
            this.arguments = e.getArguments();
            return CONTINUE_LOOP_STATUS;
        }
    }

    @Override
    public boolean shouldContinue(final Object returnValue) {
        return returnValue == CONTINUE_LOOP_STATUS;
    }
}
