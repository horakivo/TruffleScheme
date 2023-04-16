package com.ihorak.truffle.node.exprs.bbuiltin.comparison;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.comparison.LessThanBinaryNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LessThanBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization
    protected boolean doReduction(Object[] arguments,
                             @Cached ReduceComparisonNode reduceNode,
                             @Cached LessThanBinaryNode lessThenBinaryOperation) {
        return reduceNode.execute(arguments, lessThenBinaryOperation, "<");
    }
}
