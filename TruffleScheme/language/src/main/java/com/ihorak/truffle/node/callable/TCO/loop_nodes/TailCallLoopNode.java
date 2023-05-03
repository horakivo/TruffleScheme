package com.ihorak.truffle.node.callable.TCO.loop_nodes;

import com.ihorak.truffle.node.callable.DispatchUserProcedureNodeGen;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchUserProcedureNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailCallLoopNode extends SchemeNode implements RepeatingNode {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DispatchUserProcedureNode dispatchUserProcedureNode;
    @Child
    private WriteFrameSlotNode writeFrameSlotNode;

    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;

    public TailCallLoopNode(int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        dispatchUserProcedureNode = DispatchUserProcedureNodeGen.create();
        writeFrameSlotNode = WriteFrameSlotNodeGen.create(tailCallResultSlot);
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        try {
            Object[] arguments = (Object[]) frame.getObject(tailCallArgumentsSlot);
            UserDefinedProcedure procedure = (UserDefinedProcedure) frame.getObject(tailCallTargetSlot);
            var result = dispatchUserProcedureNode.executeDispatch(procedure, arguments);
            writeFrameSlotNode.executeWrite(frame, result);
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
