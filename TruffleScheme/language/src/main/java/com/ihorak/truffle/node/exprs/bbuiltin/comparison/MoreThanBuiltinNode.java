package com.ihorak.truffle.node.exprs.bbuiltin.comparison;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.comparison.MoreThanBinaryNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class MoreThanBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization
    protected boolean doReduction(Object[] arguments,
                                  @Cached ReduceComparisonNode reduceNode,
                                  @Cached MoreThanBinaryNode moreThanBinaryNode) {
        return reduceNode.execute(arguments, moreThanBinaryNode, ">");
    }
}
