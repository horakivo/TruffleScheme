package com.ihorak.truffle.node.exprs.primitive_procedure.comparison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.BinaryReducibleBuiltin;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.node.exprs.core.comperison.LessThenEqualBinaryNodeGen;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public abstract class LessThenEqualPrimitiveProcedureNode extends BinaryReducibleBuiltin {

    @SuppressWarnings("FieldMayBeFinal")
    @Child private BinaryOperationNode lessThenEqualOperation = LessThenEqualBinaryNodeGen.create();
    @SuppressWarnings("FieldMayBeFinal")
    @Child private ReduceComparisonPrimitiveProcedureNode comparisonReduceNode = ReduceComparisonPrimitiveProcedureNodeGen.create();

    @TruffleBoundary
    @Specialization(guards = "arguments.length == 0")
    protected boolean noArgs(Object[] arguments) {
        throw new SchemeException("<=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", this);
    }

    @Specialization
    protected boolean reduce(Object[] arguments) {
        return comparisonReduceNode.execute(arguments, lessThenEqualOperation);
    }
}
