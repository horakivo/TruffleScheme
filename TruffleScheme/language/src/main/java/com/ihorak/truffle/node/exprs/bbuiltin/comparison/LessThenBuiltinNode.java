package com.ihorak.truffle.node.exprs.bbuiltin.comparison;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.core.comperison.LessThenBinaryNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LessThenBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization
    protected boolean doReduction(Object[] arguments,
                             @Cached ReduceComparisonNode reduceNode,
                             @Cached LessThenBinaryNode lessThenBinaryOperation) {
        return reduceNode.execute(arguments, lessThenBinaryOperation, "<");
    }
}
