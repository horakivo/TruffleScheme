package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailCallLoopNode extends SchemeExpression implements RepeatingNode {

    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();
    private CallTarget callTarget;
    private Object[] arguments;

    public TailCallLoopNode(final CallTarget callTarget, final Object[] arguments) {
        this.callTarget = callTarget;
        this.arguments = arguments;
    }

    @Override
    public Object executeGeneric(final VirtualFrame frame) {
        return true;
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        try {
            dispatchNode.executeDispatch(callTarget, arguments);
            return false;
        } catch (TailCallException e) {
            this.callTarget = e.getCallTarget();
            this.arguments = e.getArguments();
            return true;
        }
    }
}
