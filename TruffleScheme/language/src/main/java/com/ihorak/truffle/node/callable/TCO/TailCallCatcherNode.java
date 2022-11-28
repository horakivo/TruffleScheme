package com.ihorak.truffle.node.callable.TCO;


import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;

import java.util.List;

/** Put in place of every non-tail call */
public class TailCallCatcherNode extends CallableExprNode {


//    @Child private CallableExprNode callNode;
//    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();
//
//    @Child private LoopNode loopNode;
    public TailCallCatcherNode(List<SchemeExpression> arguments, SchemeExpression callable) {
        super(arguments, callable);
    }

    @Override
    protected Object call(CallTarget callTarget, Object[] arguments) {
        while (true) {
            try {
                return super.call(callTarget, arguments);
            } catch (TailCallException e) {
                callTarget = e.getCallTarget();
                arguments = e.getArguments();
            }
        }
    }

    //    @Override
//    public Object executeGeneric(final VirtualFrame frame) {
//        try {
//            return callNode.executeGeneric(frame);
//        } catch (TailCallException e) {
//            if (loopNode == null) {
//                CompilerDirectives.transferToInterpreterAndInvalidate();
//                loopNode = insert(Truffle.getRuntime().createLoopNode(new TailCallLoopNode(e.getCallTarget(), e.getArguments())));
//            }
//
//            var test = loopNode.execute(frame);
//
//            return 0L;
//        }
//    }
}
