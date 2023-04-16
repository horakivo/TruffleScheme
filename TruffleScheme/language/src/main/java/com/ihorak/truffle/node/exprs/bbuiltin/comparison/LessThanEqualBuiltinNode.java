package com.ihorak.truffle.node.exprs.bbuiltin.comparison;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.exprs.bbuiltin.core.comparison.LessThanEqualBinaryNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LessThanEqualBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization
    protected boolean doReduction(Object[] arguments,
                                  @Cached ReduceComparisonNode reduceNode,
                                  @Cached LessThanEqualBinaryNode lessThanEqualBinaryNode) {
        return reduceNode.execute(arguments, lessThanEqualBinaryNode, "<=");
    }
}
