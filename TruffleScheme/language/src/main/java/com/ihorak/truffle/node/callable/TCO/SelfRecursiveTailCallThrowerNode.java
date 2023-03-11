package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class SelfRecursiveTailCallThrowerNode extends SchemeExpression {

    @Children
    private final WriteLocalVariableExprNode[] writeArgumentsToTemporalSlots;

    @Children
    private final WriteLocalVariableExprNode[] writeArgumentsFromTemporalSlot;


    public SelfRecursiveTailCallThrowerNode(List<WriteLocalVariableExprNode> writeArgumentsToTemporalSlots, List<WriteLocalVariableExprNode> writeArgumentsFromTemporalSlot) {
        this.writeArgumentsToTemporalSlots = writeArgumentsToTemporalSlots.toArray(WriteLocalVariableExprNode[]::new);
        this.writeArgumentsFromTemporalSlot = writeArgumentsFromTemporalSlot.toArray(WriteLocalVariableExprNode[]::new);
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame) {
        storeArgumentsInTempSlots(frame);
        writeArgsFromTempSlotsToRealSlots(frame);
        throw SelfRecursiveTailCallException.INSTANCE;
    }


    @ExplodeLoop
    private void storeArgumentsInTempSlots(VirtualFrame frame) {
        for (var expr : writeArgumentsToTemporalSlots) {
            expr.executeGeneric(frame);
        }
    }

    @ExplodeLoop
    private void writeArgsFromTempSlotsToRealSlots(VirtualFrame frame) {
        for (var expr : writeArgumentsFromTemporalSlot) {
            expr.executeGeneric(frame);
        }
    }
}
