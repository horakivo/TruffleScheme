package com.ihorak.truffle.node.builtin.comparison;

import com.ihorak.truffle.node.builtin.core.comparison.MoreThanEqualBinaryNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class MoreThanEqualBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization
    protected boolean doReduction(Object[] arguments,
                                  @Cached ReduceComparisonNode reduceNode,
                                  @Cached MoreThanEqualBinaryNode moreThanEqualBinaryNode) {
        return reduceNode.execute(arguments, moreThanEqualBinaryNode, ">=");
    }
}
