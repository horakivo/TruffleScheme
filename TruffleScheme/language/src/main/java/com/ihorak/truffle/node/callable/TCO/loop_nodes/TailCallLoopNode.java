package com.ihorak.truffle.node.callable.TCO.loop_nodes;

import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.ihorak.truffle.node.scope.StoreTailCallResultInFrameNode;
import com.ihorak.truffle.node.scope.StoreTailCallResultInFrameNodeGen;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailCallLoopNode extends SchemeNode implements RepeatingNode {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DispatchNode dispatchNode = DispatchNodeGen.create();
    @Child private StoreTailCallResultInFrameNode storeTailCallResultInFrameNode;

    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;

    public TailCallLoopNode(int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        storeTailCallResultInFrameNode = StoreTailCallResultInFrameNodeGen.create(tailCallResultSlot);
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        try {
            Object[] arguments = (Object[]) frame.getObject(tailCallArgumentsSlot);
            UserDefinedProcedure procedure = (UserDefinedProcedure) frame.getObject(tailCallTargetSlot);
            storeTailCallResultInFrameNode.execute(frame, dispatchNode.executeDispatch(procedure, arguments));
            return false;
        } catch (TailCallException e) {
            frame.setObject(tailCallTargetSlot, e.getUserDefinedProcedure());
            frame.setObject(tailCallArgumentsSlot, e.getArguments());
            return true;
        }
    }

//    @Override
//    public Object executeRepeatingWithValue(final VirtualFrame frame) {
//        try {
////            TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
////            return dispatchNode.executeDispatch(target.target, target.arguments);
//
//            Object[] arguments = (Object[]) frame.getObject(tailCallArgumentsSlot);
//            UserDefinedProcedure procedure = (UserDefinedProcedure) frame.getObject(tailCallTargetSlot);
//            return dispatchNode.executeDispatch(procedure, arguments);
//        } catch (TailCallException e) {
////            SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
//            frame.setObject(tailCallTargetSlot, e.getUserDefinedProcedure());
//            frame.setObject(tailCallArgumentsSlot, e.getArguments());
//            return CONTINUE_LOOP_STATUS;
//        }
//    }
//
//    @Override
//    public boolean shouldContinue(final Object returnValue) {
//        return returnValue == CONTINUE_LOOP_STATUS;
//    }
}
