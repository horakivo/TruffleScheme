package com.ihorak.truffle.node.callable.TCO.loop_nodes;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailCallLoopNode extends SchemeNode implements RepeatingNode {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DispatchNode dispatchNode = DispatchNodeGen.create();

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
//            TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
//            return dispatchNode.executeDispatch(target.target, target.arguments);

            Object[] arguments = (Object[]) frame.getObject(TCO_ARGUMENT_SLOT);
            CallTarget callTarget = (CallTarget) frame.getObject(TCO_CALLTARGET_SLOT);
            return dispatchNode.executeDispatch(callTarget, arguments);
        } catch (TailCallException e) {
//            SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
//            frame.setObject(tailCallTargetSlot, target.target);
//            frame.setObject(tailCallArgumentsSlot, target.arguments);
            return CONTINUE_LOOP_STATUS;
        }
    }

    @Override
    public boolean shouldContinue(final Object returnValue) {
        return returnValue == CONTINUE_LOOP_STATUS;
    }
}
