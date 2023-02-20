package com.ihorak.truffle.node.callable.TCO;


import java.util.List;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailCallLoopNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;

/**
 * Put in place of every non-tail call
 */
public class TailCallCatcherNode extends CallableExprNode {


    //    @Child private CallableExprNode callNode;
//    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();
//
    @Child private LoopNode loopNode;
    
    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;

    public TailCallCatcherNode(List<SchemeExpression> arguments, SchemeExpression callable, int tailCallArgumentsSlot, int tailCallTargetSlot) {
        super(arguments, callable);
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.loopNode = Truffle.getRuntime().createLoopNode(new TailCallLoopNode(tailCallArgumentsSlot, tailCallTargetSlot));
    }

//    @Override
//    protected Object call(CallTarget callTarget, Object[] arguments) {
//        while (true) {
//            try {
//                return super.call(callTarget, arguments);
//            } catch (TailCallException e) {
//                callTarget = e.getCallTarget();
//                arguments = e.getArguments();
//            }
//        }
//    }

    @Override
    protected Object call(CallTarget callTarget, Object[] arguments, VirtualFrame frame) {
        SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
        target.arguments = arguments;
        target.target = callTarget;



//    	frame.setObject(tailCallTargetSlot, callTarget);
//    	frame.setObject(tailCallArgumentsSlot, arguments);



		// seen a tail call can repeat that.
		return loopNode.execute(frame);
    }

//        @Override
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
