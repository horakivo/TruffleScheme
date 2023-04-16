package com.ihorak.truffle.node.exprs.bbuiltin.comparison;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.comparison.EqualNumbersBinaryNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class EqualNumbersBuiltinNode extends AlwaysInlinableProcedureNode {


    @Specialization
    protected boolean doReduction(Object[] arguments,
                                  @Cached ReduceComparisonNode reduceNode,
                                  @Cached EqualNumbersBinaryNode equalNumbersBinaryNode) {
        return reduceNode.execute(arguments, equalNumbersBinaryNode, "=");
    }
}
