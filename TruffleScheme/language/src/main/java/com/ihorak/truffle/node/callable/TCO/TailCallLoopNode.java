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

    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;
    
    public TailCallLoopNode(int tailCallArgumentsSlot, int tailCallTargetSlot) {
    	 this.tailCallArgumentsSlot = tailCallArgumentsSlot;
         this.tailCallTargetSlot = tailCallTargetSlot;
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        throw CompilerDirectives.shouldNotReachHere();
    }

    @Override
    public Object executeRepeatingWithValue(final VirtualFrame frame) {
        try {
        	Object[] arguments = (Object[])frame.getObject(tailCallArgumentsSlot);
        	CallTarget target = (CallTarget)frame.getObject(tailCallTargetSlot);
            Object result = dispatchNode.executeDispatch(target, arguments);
            return result;
        } catch (TailCallException e) {
        	frame.setObject(tailCallTargetSlot, e.getCallTarget());
        	frame.setObject(tailCallArgumentsSlot, e.getArguments());
            return CONTINUE_LOOP_STATUS;
        }
    }

    @Override
    public boolean shouldContinue(final Object returnValue) {
        return returnValue == CONTINUE_LOOP_STATUS;
    }
}
