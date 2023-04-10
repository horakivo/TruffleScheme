package com.ihorak.truffle.node.callable.TCO;


import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailCallLoopNode;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;


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
        var args = getProcedureArguments(procedure, this.arguments, frame);
        return call(procedure, args, frame);
    }

    protected Object call(UserDefinedProcedure userDefinedProcedure, Object[] arguments, VirtualFrame frame) {

        frame.setObject(tailCallTargetSlot, userDefinedProcedure);
        frame.setObject(tailCallArgumentsSlot, arguments);

        loopNode.execute(frame);

        return frame.getObject(tailCallResultSlot);
    }

}
