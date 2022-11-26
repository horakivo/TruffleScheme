package com.ihorak.truffle.node.callable.TCO;


import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

/** Put in place of every non-tail call */
public class TailCallCatcherNode extends SchemeExpression {


    @Child private CallableExprNode callNode;
    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();
    public TailCallCatcherNode(final CallableExprNode callNode) {
        this.callNode = callNode;
    }

    @Override
    public Object executeGeneric(final VirtualFrame frame) {
        try {
            return callNode.executeGeneric(frame);
        } catch (TailCallException e) {
            CallTarget callTarget = e.getCallTarget();
            Object[] arguments = e.getArguments();

            while (true) {
                try {
                    return dispatchNode.executeDispatch(callTarget, arguments);
                } catch (TailCallException e2) {
                    callTarget = e2.getCallTarget();
                    arguments = e2.getArguments();
                }
            }
        }
    }
}
