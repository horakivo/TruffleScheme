package com.ihorak.truffle.node.callable.TCO.loop_nodes;

import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class TailRecursiveCallLoopNode extends SchemeNode implements RepeatingNode {


    @Children
    private final SchemeExpression[] bodyExpressions;


    public TailRecursiveCallLoopNode(SchemeExpression[] bodyExpressions) {
        this.bodyExpressions = bodyExpressions;
    }

    @ExplodeLoop
    private void executeBody(VirtualFrame frame) {
        for (SchemeExpression bodyExpression : bodyExpressions) {
            bodyExpression.executeGeneric(frame);
        }
    }

    @Override
    public boolean executeRepeating(final VirtualFrame frame) {
        try {
            executeBody(frame);
            return false;
        } catch (SelfRecursiveTailCallException e) {
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
