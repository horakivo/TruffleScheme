package com.ihorak.truffle.node.callable.TCO;


import java.util.List;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailCallLoopNode;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.LoopNode;

/**
 * Put in place of every non-tail call
 */
public class TailCallCatcherNode extends SchemeExpression {

    @Child private LoopNode loopNode;
    @Children private final SchemeExpression[] arguments;
    @Child private SchemeExpression callable;
    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;
    private final int tailCallResultSlot;

    public TailCallCatcherNode(SchemeExpression[] arguments, SchemeExpression callable, int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.arguments = arguments;
        this.callable = callable;
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.tailCallResultSlot = tailCallResultSlot;
        this.loopNode = Truffle.getRuntime().createLoopNode(new TailCallLoopNode(tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot));
    }


    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var procedure = (UserDefinedProcedure) callable.executeGeneric(frame);
        var args = getArguments(procedure, frame);
        return call(procedure, args, frame);
    }

    protected Object call(UserDefinedProcedure userDefinedProcedure, Object[] arguments, VirtualFrame frame) {

        frame.setObject(tailCallTargetSlot, userDefinedProcedure);
        frame.setObject(tailCallArgumentsSlot, arguments);

        loopNode.execute(frame);

        return frame.getObject(tailCallResultSlot);
    }


    @ExplodeLoop
    private Object[] getArguments(UserDefinedProcedure function, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return args;
    }

}
