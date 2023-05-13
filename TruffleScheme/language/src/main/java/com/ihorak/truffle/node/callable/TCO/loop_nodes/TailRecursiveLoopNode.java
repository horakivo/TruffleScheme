package com.ihorak.truffle.node.callable.TCO.loop_nodes;

import com.ihorak.truffle.node.callable.TCO.exceptions.TailRecursiveException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailRecursiveLoopNode extends SchemeNode implements RepeatingNode {


    @Children
    private final SchemeExpression[] bodyExpressions;
    @Child
    private WriteFrameSlotNode writeFrameSlotNode;


    public TailRecursiveLoopNode(SchemeExpression[] bodyExpressions, int tailRecursiveResultIndex) {
        this.bodyExpressions = bodyExpressions;
        this.writeFrameSlotNode = WriteFrameSlotNodeGen.create(tailRecursiveResultIndex);
    }

    @ExplodeLoop
    private Object executeBody(VirtualFrame frame) {
        for (int i = 0; i < bodyExpressions.length - 1; i++) {
            bodyExpressions[i].executeGeneric(frame);
        }

        //return last element
        return bodyExpressions[bodyExpressions.length - 1].executeGeneric(frame);
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        try {
            var result = executeBody(frame);
            writeFrameSlotNode.executeWrite(frame, result);
            return false;
        } catch (TailRecursiveException e) {
            return true;
        }
    }

//    @Override
//    public Object executeRepeatingWithValue(final VirtualFrame frame) {
//        try {
////            Object[] arguments = (Object[]) frame.getObject(argumentsIndex);
////            var virtualFrame = Truffle.getRuntime().createVirtualFrame(arguments, frameDescriptor);
//            return executeImpl(frame);
//        } catch (SelfRecursiveTailCallException e) {
////            SchemeTruffleLanguage.TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
////            frame.setObject(argumentsIndex, target.arguments);
//            return CONTINUE_LOOP_STATUS;
//        }
//    }

//    @Override
//    public boolean shouldContinue(final Object returnValue) {
//        return returnValue == CONTINUE_LOOP_STATUS;
//    }

}
